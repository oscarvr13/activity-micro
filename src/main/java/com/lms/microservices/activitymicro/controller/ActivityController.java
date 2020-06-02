package com.lms.microservices.activitymicro.controller;

import com.lms.microservices.activitymicro.controller.api.ActivityApi;
import com.lms.microservices.activitymicro.dto.ActivityDto;
import com.lms.microservices.activitymicro.dto.PropertiesDto;
import com.lms.microservices.activitymicro.services.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RequestMapping(value = "${path.request.mapping.questionnaire.controller}")
@RestController
@Api(value = "Activity Controller", description = "All the activities endpooints")
public class ActivityController implements ActivityApi {

  private final ActivityService activityService;

  private final PropertiesDto propertiesDto;

  @PutMapping("/putTest")
  @Override
  public ResponseEntity<List<ActivityDto>> getTest() {
    throw new NullPointerException();
  }

  @Override
  public ResponseEntity<PropertiesDto> getProperties() {
    System.out.println("Get properties");
    return ResponseEntity.ok(propertiesDto);
  }

  @ApiOperation(value = "To retrieve all the available activities",
      nickname = "retrieveAllActivities",
      notes = "This API is used to find and retrieve the detail"
          + " of all the available activities",
      //response = ActivityDto.class,
      //responseContainer = "List")
      response = ResponseEntity.class)
  @ApiImplicitParams({
      @ApiImplicitParam(value = "Country code in 2 character ISO 3166 format", required = true,
          name = "countryCode", dataType = "String", paramType = "header"),
      @ApiImplicitParam(name = "Accept",
          value = "Content-Types that are acceptable for the response", required = true,
          dataType = "String", paramType = "header"),
  })
  public ResponseEntity<List<ActivityDto>> getActivities(
      Map<String, String> headers, List<Integer> idList) {
    System.out.println(headers);
    if(idList==null) {
      return activityService.getAllActivities();
    }else{
      return activityService.getAllActivitiesById(idList);
    }
  }

  @ApiOperation(value = "To retrieve the required activity",
      nickname = "retrieveActivityById",
      notes = "This API is used to find and retrieve the required activity "
          + " specified by the Id",
      //response = ActivityDto.class,
      response = ResponseEntity.class,
      tags = "retrieve-activity-by-Id")
  @ApiImplicitParams({
      @ApiImplicitParam(value = "Country code in 2 character ISO 3166 format", required = true,
          name = "countryCode", dataType = "String", paramType = "header"),
      @ApiImplicitParam(name = "Accept",
          value = "Content-Types that are acceptable for the response", required = true,
          dataType = "String", paramType = "header"),
  })
  @Override
  public ResponseEntity<ActivityDto> getActivity(@ApiParam(
      value = "ActivityId generated by the system ",
      required = true) int activityId) {
    System.out.println("Obtiene");
    return activityService.getActivity(activityId);
  }

  @ApiOperation(value = "To insert the new activity",
      nickname = "insertNewActivity",
      notes = "This API is used to insert a new activity ",
      response = ResponseEntity.class,
      tags = "insert-activity")
  @ApiImplicitParams({
      @ApiImplicitParam(value = "Country code in 2 character ISO 3166 format", required = true,
          name = "countryCode", dataType = "String", paramType = "header"),
      @ApiImplicitParam(name = "Accept",
          value = "Content-Types that are acceptable for the response", required = true,
          dataType = "String", paramType = "header"),
      @ApiImplicitParam(name = "Content-Type",
          value = "Content-Types that are acceptable for the request", required = true,
          dataType = "String", paramType = "header")
  })
  @Override
  public ResponseEntity<ActivityDto> insertActivity(@Valid ActivityDto newActivity) {
    System.out.println("Inserta");
    ActivityDto activityDto = activityService.insertActivity(newActivity);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(activityDto.getId()).toUri();
    return ResponseEntity.created(location).body(activityDto);
  }

  @ApiOperation(value = "To update activity",
      nickname = "updateActivity",
      notes = "This API is used to update an existing activity",
      response = ResponseEntity.class,
      tags = "update-activity")
  @ApiImplicitParams({
      @ApiImplicitParam(value = "Country code in 2 character ISO 3166 format", required = true,
          name = "countryCode", dataType = "String", paramType = "header"),
      @ApiImplicitParam(name = "Accept",
          value = "Content-Types that are acceptable for the response", required = true,
          dataType = "String", paramType = "header"),
      @ApiImplicitParam(name = "Content-Type",
          value = "Content-Types that are acceptable for the request", required = true,
          dataType = "String", paramType = "header")
  })
  @Override
  public ResponseEntity<ActivityDto> updateActivity(
      int activityId, @Valid ActivityDto updatedActivity) {
    System.out.println("Entra update");
    ActivityDto activityDto = activityService.updateActivity(activityId, updatedActivity);
    return ResponseEntity.ok(activityDto);
  }

  @ApiOperation(value = "To delete activity",
      nickname = "deleteActivity",
      notes = "This API is used to delete an existing activity or",
      response = ResponseEntity.class,
      tags = "delete-activity")
  @ApiImplicitParams({
      @ApiImplicitParam(value = "Country code in 2 character ISO 3166 format", required = true,
          name = "countryCode", dataType = "String", paramType = "header")
  })
  @Override
  public ResponseEntity deleteActivity(int activityId) {
    activityService.deleteActivity(activityId);
    return ResponseEntity.noContent().build();
  }
}
