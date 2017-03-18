package org.mattreyuk.adtech.client;

import javax.annotation.concurrent.Immutable;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(builder = ProviderRequest.ProviderRequestBuilder.class)
@Immutable
public class ProviderRequest {
	private Integer width;
	private Integer height;
	private String domain;
	private String userip;
	private String useragent;
	
	  @JsonPOJOBuilder(withPrefix = "")
	  public static final class ProviderRequestBuilder {
	  }

}
