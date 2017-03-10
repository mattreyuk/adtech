package org.mattreyuk.adtech.domain;

import java.net.URL;

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
@JsonDeserialize(builder = Provider.ProviderBuilder.class)
@Immutable
public class Provider {
	private Integer provider_id;
	private URL url;

	@JsonPOJOBuilder(withPrefix = "")
	public static final class ProviderBuilder {
	}

}
