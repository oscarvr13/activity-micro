package com.lms.microservices.activitymicro.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
@ApiModel(value = "ErrorResponse", description = "Error custom response")
public class ErrorDetails {
  @ApiModelProperty(value = "Date and time of the error",
      example = "2020-05-13T18:58:47.199512", required = true)
  private final LocalDateTime timestamp;

  @ApiModelProperty(value = "Main message of the error",
      example = " Activity Not Found", required = true)
  private final String message;

  @ApiModelProperty(value = "Details of the error",
      example = " Activity Not Found", required = true)
  private final String details;


}
