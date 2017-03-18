package org.mattreyuk.adtech.service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.mattreyuk.adtech.client.ProviderRequest;
import org.mattreyuk.adtech.client.ProviderCommand;
import org.mattreyuk.adtech.dal.AdtechDal;
import org.mattreyuk.adtech.dal.HistoryDal;
import org.mattreyuk.adtech.domain.AdMessage;
import org.mattreyuk.adtech.domain.Bid;
import org.mattreyuk.adtech.domain.Provider;
import org.mattreyuk.adtech.domain.Transaction;
import org.mattreyuk.adtech.domain.Transaction.ClickResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import rx.Observable;
import rx.schedulers.Schedulers;

@Service
public class AdtechService {

	@Value("${CLICKTIMER:3}")
	long clicktimeout;

	private final AdtechDal adtechDal;
	private final HistoryDal historyDal;
	private final RestTemplate restTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdtechService.class);

	@Autowired
	public AdtechService(AdtechDal adtechDal, HistoryDal historyDal, RestTemplate restTemplate) {
		this.adtechDal = adtechDal;
		this.historyDal = historyDal;
		this.restTemplate = restTemplate;
	}

	public AdMessage findAd(Integer user_id, Integer width, Integer height, URL url, String userAgent, String userip) {
		List<Provider> providers;
		LOGGER.info("getting ads for user: {}, width: {}, height: {}", user_id, width, height);
		providers = adtechDal.findProviders(user_id, width, height);
		LOGGER.debug("found: {}", providers);

		if (providers != null && !providers.isEmpty()) {
			ProviderRequest req = ProviderRequest.builder().width(width).height(height).useragent(userAgent).
					userip(userip).domain(url.getHost()).build();

			//stream the providers, fan out (max of 10 at a time) to request bids via Hystrix command
			//log any errors and keep going
			Observable<Bid> obids = Observable.from(providers).flatMap(p -> {
				return new ProviderCommand(restTemplate, p, req).toObservable()
						.subscribeOn(Schedulers.io())
						.doOnError(e -> LOGGER.warn("Failed during provider request {}", p.getProvider_name(), e))
						.onErrorResumeNext(e -> Observable.empty());}, 10);
			
			//now sort the streamed bids and transform back to regular list.
			//We break this up so the blocking doesn't short circuit the fan out on requests
			List<Bid>bids=obids.toSortedList(Bid::comparePrice)
					.filter(f->f!=null)
					.toBlocking()
					.first();


			LOGGER.info("Bids received: {}",bids);
			
			UUID tid = UUID.randomUUID();
			Bid winningBid=bids.get(bids.size()-1);
			Transaction tx=Transaction.builder()
					.adTime(LocalDateTime.now()).bids(bids).clickResult(ClickResult.REQUEST)
					.transactionId(tid).userid(user_id).winningPrice(winningBid.getBidPrice())
					.winningProvider(winningBid.getProviderId()).build();
			
			historyDal.addTransaction(tx);

			return AdMessage.builder().tid(tid).html(winningBid.getHtml()).build();
			
		}

		return null;
	}

	public List<Transaction> getTransactions() {
		LocalDateTime now = LocalDateTime.now();

		List<Transaction> transactions = historyDal.getCurrentHistory();
		// update all transactions that have gone stale
		transactions.replaceAll(t -> t.getClickResult()
				.equals(ClickResult.REQUEST) && t.getAdTime()
				.plusSeconds(clicktimeout)
				.isBefore(now) ? t.withClickResult(ClickResult.STALE) : t);

		return transactions;
	}

	public void registerClick(UUID tid, Integer userid, LocalDateTime now) {
		LocalDateTime adTime = historyDal.getTransactionTime(tid);
		if (adTime != null) {
			ClickResult clickResult;
			if (adTime.plusSeconds(clicktimeout)
					.isBefore(now)) {
				clickResult = ClickResult.STALE;
			} else {
				clickResult = ClickResult.CLICK;
			}
			LOGGER.debug("updating tid {} for user {} with status {}", tid, userid, clickResult);
			historyDal.updateClickStatus(tid, clickResult);
		} else {
			LOGGER.info("received click on tid {} for user {} that no longer is in history", tid, userid);
		}
	}

}
