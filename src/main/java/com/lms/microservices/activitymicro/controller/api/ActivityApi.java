package com.lms.microservices.activitymicro.controller.api;

import com.lms.microservices.activitymicro.dto.ActivityDto;
import java.util.List;
import java.util.Map;

import com.lms.microservices.activitymicro.dto.PropertiesDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

public interface ActivityApi {


  @GetMapping(value = "/getTestNoBranch",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_ATOM_XML_VALUE
      })
  ResponseEntity<List<ActivityDto>> getTestBranch();

  @GetMapping(value = "/getTest",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_ATOM_XML_VALUE
      })
  ResponseEntity<List<ActivityDto>> getTest();

  @GetMapping(value = "/getProperties",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_ATOM_XML_VALUE
      })
  ResponseEntity<PropertiesDto> getProperties();


  @GetMapping(value = "${path.request.mapping.request.activities}",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_ATOM_XML_VALUE
      })
  ResponseEntity<List<ActivityDto>> getActivities(
      @ApiIgnore @RequestHeader Map<String, String> headers,
      @RequestParam(value = "id", required = false) List<Integer> idList);

  @GetMapping(value = "${path.request.mapping.request.activity}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
  ResponseEntity<ActivityDto> getActivity(@PathVariable int activityId);


  @PostMapping(value = "${path.request.mapping.request.activities}",
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
  ResponseEntity<ActivityDto> insertActivity(@RequestBody ActivityDto newActivity);

  @PutMapping(value = "${path.request.mapping.put.activity}",
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
  ResponseEntity<ActivityDto> updateActivity(
      @PathVariable int activityId, @RequestBody ActivityDto newActivity);

  @DeleteMapping(value = "${path.request.mapping.delete.activity}")
  ResponseEntity deleteActivity(@PathVariable int activityId);

}
