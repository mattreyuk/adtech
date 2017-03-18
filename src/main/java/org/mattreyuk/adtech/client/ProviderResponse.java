package org.mattreyuk.adtech.client;

import java.math.BigDecimal;

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
@JsonDeserialize(builder = ProviderResponse.ProviderResponseBuilder.class)
@Immutable
public class ProviderResponse {
	private BigDecimal bidprice;
	private String adhtml;
	
	  @JsonPOJOBuilder(withPrefix = "")
	  public static final class ProviderResponseBuilder {
	  }
	
}
