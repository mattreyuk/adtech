package org.mattreyuk.adtech.service;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.mattreyuk.adtech.dal.AdtechDal;
import org.mattreyuk.adtech.dal.HistoryDal;
import org.mattreyuk.adtech.domain.Transaction;
import org.mattreyuk.adtech.domain.Transaction.ClickResult;
import org.mockito.Mockito;

public class AdtechServiceTest {

	private AdtechDal mockatDal = Mockito.mock(AdtechDal.class);
	private HistoryDal mockhDal = Mockito.mock(HistoryDal.class);
	
	AdtechService testService = new AdtechService(mockatDal,mockhDal);

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
}
