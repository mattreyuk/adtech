package org.mattreyuk.adtech.domain;

import java.util.UUID;

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
@JsonDeserialize(builder = AdMessage.AdMessageBuilder.class)
@Immutable
public class AdMessage {

	private UUID tid;
	private String html;
	
	  @JsonPOJOBuilder(withPrefix = "")
	  public static final class AdMessageBuilder {
	  }

}
