package com.lms.microservices.activitymicro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.lms.microservices.activitymicro.dto.ActivityDto;
import com.lms.microservices.activitymicro.exception.ActivityNotFoundException;
import com.lms.microservices.activitymicro.services.ActivityService;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@NoArgsConstructor
@RunWith(SpringRunner.class)
@WebMvcTest(value = ActivityController.class)
public class ActivityControllerTest {

    @Autowired
    private  MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;


    @Test
    public void testGetActivities_basic() throws Exception {
        when(activityService.getAllActivities())
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(Arrays
                        .asList(ActivityDto.builder().id(1).name("Java Code")
                                        .points(5).build(),
                                ActivityDto.builder().id(2).name("Run Scripts")
                                        .points(8).build(),
                                ActivityDto.builder().id(3).name("Activity 3")
                                        .points(9).build()
                        )));

        RequestBuilder request = MockMvcRequestBuilders.get("/v1/lms/activities")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String stringResponse = result.getResponse().getContentAsString();
        DocumentContext context = JsonPath.parse(stringResponse);
        List<ActivityDto> activities = context.read("$");
        assertThat(activities).hasSize(3);
        assertThat(activities).extracting("id")
                .containsExactly(1, 2, 3);
        assertThat(activities).extracting("name")
                .containsExactly("Java Code", "Run Scripts", "Activity 3");
    }

    @Test
    public void testGetActivities_checkEmptyList() throws Exception {
        when(activityService.getAllActivities())
                .thenReturn(ResponseEntity.ok(Collections.EMPTY_LIST));

        RequestBuilder request = MockMvcRequestBuilders.get("/v1/lms/activities")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String stringResponse = result.getResponse().getContentAsString();
        DocumentContext context = JsonPath.parse(stringResponse);
        List<ActivityDto> activities = context.read("$");
        assertThat(activities).hasSize(0);
    }

    @Test
    public void testGetActivities_checkContentType() throws Exception {
        when(activityService.getAllActivities())
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(Arrays
                        .asList(ActivityDto.builder().id(1).name("Java Code")
                                        .points(5).build(),
                                ActivityDto.builder().id(2).name("Run Scripts")
                                        .points(8).build(),
                                ActivityDto.builder().id(3).name("Activity 3")
                                        .points(9).build()
                        )));

        RequestBuilder request = MockMvcRequestBuilders.get("/v1/lms/activities")
                .accept(MediaType.APPLICATION_ATOM_XML);
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_ATOM_XML))
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        System.out.println("Result: "+resultString);
    }

    @Test
    public void testGetActivity_basic() throws Exception {
        when(activityService.getActivity(3))
                .thenReturn(ResponseEntity.ok()
                        .body(ActivityDto.builder().id(3).name("Activity 3")
                                        .points(9).build()
                        ));
        RequestBuilder request = MockMvcRequestBuilders.get("/v1/lms/activities/3")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String stringResponse = result.getResponse().getContentAsString();
        System.out.println("Response: "+ stringResponse);
        ObjectMapper mapper = new ObjectMapper();
        ActivityDto activityDto = mapper.readValue(stringResponse, ActivityDto.class);
        assertThat(activityDto).extracting("id", "name", "points")
                .containsExactlyInAnyOrder(3, "Activity 3", 9);
    }

    @Test
    public void testGetActivity_NotFoundException() throws Exception {
        when(activityService.getActivity(3))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found Activity by Id"));
        RequestBuilder request = MockMvcRequestBuilders.get("/v1/lms/activities/3")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andExpect(status().isNotFound())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
               // .andExpect(content().json("{\"message\": \"Not Found Activity by Id\"}", false))
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        System.out.println(result);
        System.out.println(resultString);
    }

    @Test
    public void testInsertActivity_basic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ActivityDto activityDtoToInsert = ActivityDto.builder()
                .name("Activity 1").points(2).build();
        URI location = URI.create("http://localhost/v1/lms/activities/1");
        when(activityService.insertActivity(activityDtoToInsert))
                .thenReturn(ActivityDto.builder().id(1).name("Activity 1")
                                .points(2).build());
        RequestBuilder request = MockMvcRequestBuilders.post("/v1/lms/activities")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(activityDtoToInsert));
        MvcResult result = mockMvc.perform(request).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "http://localhost/v1/lms/activities/1"))
                .andReturn();
        String stringResponse = result.getResponse().getContentAsString();
        ActivityDto activityDtoInserted = mapper.readValue(stringResponse, ActivityDto.class);
        assertThat(activityDtoInserted).extracting("id", "name", "points")
                .containsExactlyInAnyOrder(1, "Activity 1", 2);
    }


    @Test
    public void testInsertActivity_BadRequest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ActivityDto activityDtoToInsert = ActivityDto.builder()
                .name("").points(2).build();
        URI location = URI.create("http://localhost:8080/v1/lms/activities/1");
        when(activityService.insertActivity(activityDtoToInsert))
                .thenReturn(ActivityDto.builder().id(1).name("")
                                .points(2).build());
        RequestBuilder request = MockMvcRequestBuilders.post("/v1/lms/activities")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(activityDtoToInsert));
        MvcResult result = mockMvc.perform(request).andExpect(status().isBadRequest())
                .andReturn();
        Exception response = result.getResolvedException();
        System.out.println("Response: "+response.getMessage());
    }


    @Test
    public void testUpdateActivity_Basic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ActivityDto activityDtoToUpdate = ActivityDto.builder()
                .name("Activity 2").points(4).build();
        when(activityService.updateActivity(2, activityDtoToUpdate))
                .thenReturn(ActivityDto.builder().id(2)
                        .name(activityDtoToUpdate.getName())
                        .points(activityDtoToUpdate.getPoints()).build());
        RequestBuilder request = MockMvcRequestBuilders.put("/v1/lms/activities/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(activityDtoToUpdate));
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String stringResponse = result.getResponse().getContentAsString();
        ActivityDto activityDtoUpdated = mapper.readValue(stringResponse, ActivityDto.class);
        assertThat(activityDtoUpdated).extracting("id", "name", "points")
                .containsExactlyInAnyOrder(2, "Activity 2", 4);
    }

    @Test
    public void testUpdateActivity_NoExistsActivity() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ActivityDto activityDtoToUpdate = ActivityDto.builder()
                .name("Activity 3").points(6).build();
        when(activityService.updateActivity(3, activityDtoToUpdate))
                .thenThrow(new ActivityNotFoundException("No se encontro una actividad con ese id"));
        RequestBuilder request = MockMvcRequestBuilders.put("/v1/lms/activities/3")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(activityDtoToUpdate));
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testDeleteActivity_Basic() throws Exception {
        when(activityService.deleteActivity(3))
                .thenReturn(true);
        RequestBuilder request = MockMvcRequestBuilders.delete("/v1/lms/activities/3");
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDeleteActivity_NoExistsActivity() throws Exception {
        when(activityService.deleteActivity(19))
                .thenThrow(new ActivityNotFoundException("No se encontro una actividad con ese id"));
        RequestBuilder request = MockMvcRequestBuilders.delete("/v1/lms/activities/19");
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
