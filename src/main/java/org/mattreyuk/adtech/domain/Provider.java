package org.mattreyuk.adtech.domain;

import java.net.URL;

import javax.annotation.concurrent.Immutable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Immutable
public class Provider {
	private Integer provider_id;
	private String provider_name;
	private URL url;
}
