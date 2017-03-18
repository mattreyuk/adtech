package org.mattreyuk.adtech.domain;

import java.math.BigDecimal;

import javax.annotation.concurrent.Immutable;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(builder = Bid.BidBuilder.class)
@Immutable
public class Bid {
	private Integer providerId;
	private BigDecimal bidPrice;
	@JsonIgnore
	private String html;

	public static Integer comparePrice(Bid bid1, Bid bid2) {
		return bid1.bidPrice.compareTo(bid2.bidPrice);
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static final class BidBuilder {
	}

}
