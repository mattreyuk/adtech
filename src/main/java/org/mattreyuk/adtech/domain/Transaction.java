package org.mattreyuk.adtech.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.annotation.concurrent.Immutable;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(builder = Transaction.TransactionBuilder.class)
@Immutable
public class Transaction {

	public enum ClickResult{
		REQUEST,
		CLICK,
		STALE
	}
	
	private UUID transactionId;
	private Integer userid;
	private @Singular List<Bid> bids;
	private BigDecimal winningPrice;
	private Integer winningProvider;
	private ClickResult clickResult;
	
	  @JsonPOJOBuilder(withPrefix = "")
	  public static final class TransactionBuilder {
	  }

}
