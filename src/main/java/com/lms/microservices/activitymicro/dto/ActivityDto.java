package com.lms.microservices.activitymicro.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel(value = "ActivityModel", description = "Values of each activity")
public class ActivityDto {


  @ApiModelProperty(example = "1", required = true, value = "Indicates tha activity Id")
  private int id;

  @ApiModelProperty(example = "Actividad 1", required = true, value = "Indicates the activity name")
  @NotEmpty(message = "Activity name must have a value")
  private String name;

  @ApiModelProperty(example = "10", required = true,
      value = "Indicates the points given to the activity")
  @NotNull(message = "points can not be null")
  @PositiveOrZero(message = "points must be a number between 0 and 9")
  @Max(value = 10, message = "points must not be greater than 10")
  private Integer points;

}


