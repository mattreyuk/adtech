package org.mattreyuk.adtech.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.mattreyuk.adtech.client.ProviderRequest;
import org.mattreyuk.adtech.client.ProviderResponse;
import org.mattreyuk.adtech.dal.AdtechDal;
import org.mattreyuk.adtech.dal.HistoryDal;
import org.mattreyuk.adtech.domain.AdMessage;
import org.mattreyuk.adtech.domain.Bid;
import org.mattreyuk.adtech.domain.Provider;
import org.mattreyuk.adtech.domain.Transaction;
import org.mattreyuk.adtech.domain.Transaction.ClickResult;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AdtechServiceTest {

	private AdtechDal mockatDal = Mockito.mock(AdtechDal.class);
	private HistoryDal mockhDal = Mockito.mock(HistoryDal.class);
	private RestTemplate mockTemplate = Mockito.mock(RestTemplate.class);
	
	AdtechService testService = new AdtechService(mockatDal,mockhDal,mockTemplate);

	@Test
	public void testRegisterClickClick() {
		testService.clicktimeout=5;
		UUID testid = UUID.randomUUID();
		LocalDateTime adTime = LocalDateTime.now();
		LocalDateTime clickTime = adTime.plusSeconds(2);
		Mockito.when(mockhDal.getTransactionTime(testid)).thenReturn(adTime);
		testService.registerClick(testid, 123,clickTime);
		Mockito.verify(mockhDal).updateClickStatus(testid, ClickResult.CLICK);
	}

	@Test
	public void testRegisterClickStale() {
		testService.clicktimeout=3;
		UUID testid = UUID.randomUUID();
		LocalDateTime adTime = LocalDateTime.now();
		LocalDateTime clickTime = adTime.plusSeconds(5);
		Mockito.when(mockhDal.getTransactionTime(testid)).thenReturn(adTime);
		testService.registerClick(testid, 123,clickTime);
		Mockito.verify(mockhDal).updateClickStatus(testid, ClickResult.STALE);
	}

	@Test
	public void testGetTransactions(){
		testService.clicktimeout=10;
		UUID testid1 = UUID.randomUUID();
		UUID testid2 = UUID.randomUUID();
		UUID testid3 = UUID.randomUUID();
		LocalDateTime ad1Time = LocalDateTime.now();
		LocalDateTime ad2Time = ad1Time.minusSeconds(15);
		ArrayList<Transaction>testTransactions=new ArrayList<Transaction>();
		List<Transaction>resultTransactions;
		Transaction t1 = Transaction.builder().adTime(ad1Time).clickResult(ClickResult.REQUEST).transactionId(testid1).build();
		Transaction t2 = Transaction.builder().adTime(ad1Time).clickResult(ClickResult.CLICK).transactionId(testid2).build();
		Transaction t3 = Transaction.builder().adTime(ad2Time).clickResult(ClickResult.REQUEST).transactionId(testid3).build();
		testTransactions.add(t1);
		testTransactions.add(t2);
		testTransactions.add(t3);
		Mockito.when(mockhDal.getCurrentHistory()).thenReturn(testTransactions);
		resultTransactions=testService.getTransactions();
		
		assertEquals(ClickResult.REQUEST,resultTransactions.get(0).getClickResult());
		assertEquals(ClickResult.CLICK,resultTransactions.get(1).getClickResult());
		assertEquals(ClickResult.STALE,resultTransactions.get(2).getClickResult());
		
	}
	
	@Test
	public void testFindAd() throws MalformedURLException{
	
		ArrayList<Provider>testProviders= new ArrayList<Provider>();
		Integer userId = 100;
		Integer width=200;
		Integer height=300;
		Integer pid1 = 10;
		Integer pid2 = 20;
		Integer pid3 = 30;
		String pn1 = "provider1";
		String pn2 = "provider2";
		String pn3 = "provider3";
		String userAgent = "useragent";
		String userip = "1.1.1.1";
		URL userUrl = new URL("http://user.com/ad");
		URL url1 = new URL("http://provider1.com/ad");
		URL url2 = new URL("http://provider2.com/ad");
		URL url3 = new URL("http://provider3.com/ad");
		testProviders.add(Provider.builder().provider_id(pid1).provider_name(pn1).url(url1).build());
		testProviders.add(Provider.builder().provider_id(pid2).provider_name(pn2).url(url2).build());
		testProviders.add(Provider.builder().provider_id(pid3).provider_name(pn3).url(url3).build());
		
		Bid bid1 = Bid.builder().bidPrice(new BigDecimal("0.0035")).html("<h1></h1>").providerId(pid1).build();
		Bid bid2 = Bid.builder().bidPrice(new BigDecimal("0.0055")).html("<h2></h2>").providerId(pid2).build();
		Bid bid3 = Bid.builder().bidPrice(new BigDecimal("0.0045")).html("<h3></h3>").providerId(pid3).build();
		ProviderResponse res1 = ProviderResponse.builder().adhtml("<h1></h1>").bidprice(new BigDecimal("0.0035")).build();
		ProviderResponse res2 = ProviderResponse.builder().adhtml("<h2></h2>").bidprice(new BigDecimal("0.0055")).build();
		ProviderResponse res3 = ProviderResponse.builder().adhtml("<h3></h3>").bidprice(new BigDecimal("0.0045")).build();
		ResponseEntity<ProviderResponse> re1 = new ResponseEntity<ProviderResponse>(res1, HttpStatus.OK);
		ResponseEntity<ProviderResponse> re2 = new ResponseEntity<ProviderResponse>(res2, HttpStatus.OK);
		ResponseEntity<ProviderResponse> re3 = new ResponseEntity<ProviderResponse>(res3, HttpStatus.OK);
		
		Mockito.when(mockatDal.findProviders(userId, width, height)).thenReturn(testProviders);
		
		HttpEntity<ProviderRequest> httpRequest= new HttpEntity<ProviderRequest>(ProviderRequest.builder().width(width).height(height).useragent(userAgent).
				userip(userip).domain(userUrl.getHost()).build());
		Mockito.when(mockTemplate.exchange(url1.toExternalForm(), HttpMethod.POST,httpRequest, ProviderResponse.class)).thenReturn(re1);
		Mockito.when(mockTemplate.exchange(url2.toExternalForm(), HttpMethod.POST,httpRequest, ProviderResponse.class)).thenReturn(re2);
		Mockito.when(mockTemplate.exchange(url3.toExternalForm(), HttpMethod.POST,httpRequest, ProviderResponse.class)).thenReturn(re3);
		
		AdMessage ad = testService.findAd(userId, width, height, userUrl, userAgent, userip);
		
		assertEquals("<h2></h2>",ad.getHtml());
		
		ArgumentCaptor<Transaction> argc = ArgumentCaptor.forClass(Transaction.class);
		
		Mockito.verify(mockhDal).addTransaction(argc.capture());
		
		assertEquals(bid1, argc.getValue().getBids().get(0));
		assertEquals(bid3, argc.getValue().getBids().get(1));
		assertEquals(bid2, argc.getValue().getBids().get(2));
		assertEquals(bid2.getBidPrice(),argc.getValue().getWinningPrice());
		assertEquals(bid2.getProviderId(),argc.getValue().getWinningProvider());
	}
}
