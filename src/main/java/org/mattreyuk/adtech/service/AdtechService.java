package org.mattreyuk.adtech.service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.mattreyuk.adtech.dal.AdtechDal;
import org.mattreyuk.adtech.dal.HistoryDal;
import org.mattreyuk.adtech.domain.AdMessage;
import org.mattreyuk.adtech.domain.Provider;
import org.mattreyuk.adtech.domain.Transaction;
import org.mattreyuk.adtech.domain.Transaction.ClickResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdtechService {

	@Value("${CLICKTIMER:3}")
	long clicktimeout;

	private final AdtechDal adtechDal;
	private final HistoryDal historyDal;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdtechService.class);
	
	  @Autowired
	  public AdtechService(AdtechDal adtechDal,HistoryDal historyDal){
		  this.adtechDal=adtechDal;
		  this.historyDal=historyDal;
	  }
	
	public AdMessage findAd(Integer user_id, Integer width, Integer height, URL url, String userAgent, String userip) {
		List<Provider> providers;
		LOGGER.info("getting ads for user: {}, width: {}, height: {}",user_id,width,height);
		providers=adtechDal.findProviders(user_id, width, height);
		LOGGER.info("found: {}",providers);
		
		return null;
	}

	public List<Transaction> getTransactions() {
		LocalDateTime now = LocalDateTime.now();
		
		List<Transaction>transactions = historyDal.getCurrentHistory();
		//update all transactions that have gone stale
		transactions.replaceAll(t->t.getClickResult().equals(ClickResult.REQUEST) && 
				t.getAdTime().plusSeconds(clicktimeout).isBefore(now) ?
				t.withClickResult(ClickResult.STALE) : t);
		
		return transactions;
	}

	public void registerClick(UUID tid, Integer userid, LocalDateTime now) {
		LocalDateTime adTime = historyDal.getTransactionTime(tid);
		if(adTime!=null){
			ClickResult clickResult;
			if (adTime.plusSeconds(clicktimeout).isBefore(now)){
				clickResult=ClickResult.STALE;
			}else{
				clickResult=ClickResult.CLICK;
			}
			LOGGER.debug("updating tid {} for user {} with status {}",tid,userid,clickResult);
			historyDal.updateClickStatus(tid, clickResult);
		}else{
			LOGGER.info("received click on tid {} for user {} that no longer is in history",tid,userid);			
		}
	}

}
