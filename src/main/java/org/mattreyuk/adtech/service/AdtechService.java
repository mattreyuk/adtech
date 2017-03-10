package org.mattreyuk.adtech.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mattreyuk.adtech.dal.AdtechDal;
import org.mattreyuk.adtech.domain.AdMessage;
import org.mattreyuk.adtech.domain.Provider;
import org.mattreyuk.adtech.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdtechService {

	private final AdtechDal adtechDal;
	private static final Logger LOGGER = LoggerFactory.getLogger(AdtechService.class);
	
	  @Autowired
	  public AdtechService(AdtechDal adtechDal){
		  this.adtechDal=adtechDal;
	  }
	
	public AdMessage findAd(Integer user_id, Integer width, Integer height, URL url, String userAgent, String userip) {
		List<Provider> providers;
		LOGGER.info("getting ads for user: {}, width: {}, height: {}",user_id,width,height);
		providers=adtechDal.findProviders(user_id, width, height);
		LOGGER.info("found: {}",providers);
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
