package org.mattreyuk.adtech.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(builder = ErrorMessage.ErrorMessageBuilder.class)
public class ErrorMessage {

  private final String code;
  private final String message;

  @JsonPOJOBuilder(withPrefix = "")
  public static final class ErrorMessageBuilder {
  }
}
