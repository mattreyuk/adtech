package org.mattreyuk.adtech.service;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.mattreyuk.adtech.domain.AdMessage;
import org.mattreyuk.adtech.domain.Transaction;
import org.springframework.stereotype.Service;

@Service
public class AdtechService {

	public AdMessage findAd(Integer userid, Integer width, Integer height, URL url, String userAgent, String userip) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Transaction> getTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	public Void registerClick(UUID tid, Integer userid) {
		// TODO Auto-generated method stub
		return null;
	}

}
