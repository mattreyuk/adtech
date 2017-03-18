package org.mattreyuk.adtech.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mattreyuk.adtech.domain.Bid;
import org.mattreyuk.adtech.domain.Provider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rx.Observable;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixObservableCommand;

public class ProviderCommand extends HystrixObservableCommand<Bid>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderCommand.class);

	private RestTemplate restTemplate;
	private Provider provider;
	private ProviderRequest request;

	public ProviderCommand(RestTemplate restTemplate, Provider provider, ProviderRequest request) {
		//group as providers but separate out circuit breaker fn by individual provider
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Providers"))
			      .andCommandKey(HystrixCommandKey.Factory.asKey(provider.getProvider_name())));
		this.restTemplate=restTemplate;
		this.provider=provider;
		this.request=request;
	}

	@Override
	protected Observable<Bid> construct() {
		LOGGER.info("calling provider: {} with request: {}",provider,request);
		
		HttpEntity<ProviderRequest> httpRequest = new HttpEntity<>(request);
	    ResponseEntity<ProviderResponse> response = restTemplate.exchange(
	    		provider.getUrl().toExternalForm(), HttpMethod.POST,httpRequest, ProviderResponse.class);
	    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
	      return Observable.empty();
	    }
	    ProviderResponse r = response.getBody();
	    return Observable.just(
	    		Bid.builder().bidPrice(r.getBidprice()).html(r.getAdhtml()).providerId(provider.getProvider_id()).build());
	}

}
