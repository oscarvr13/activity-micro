package com.lms.microservices.activitymicro;

import com.lms.microservices.activitymicro.dto.ActivityDto;
import com.lms.microservices.activitymicro.exception.ErrorDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ActivityControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;
    private Object ResponseEntity;

    @Test
    public void testGetAllActivities() {
        /*
        List<ActivityDto> activities = Arrays.asList(this.restTemplate
                .getForObject("/v1/lms/activities", ActivityDto[].class));
        assertThat(activities).extracting("id").containsExactly(1, 2, 3);
         */
        ResponseEntity<ActivityDto[]> response = this.restTemplate
                .getForEntity(URI.create("/v1/lms/activities"), ActivityDto[].class);
        List<ActivityDto> activities = Arrays.asList(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(activities).hasSize(3).extracting("id").containsExactly(1, 2 ,3);
        assertThat(activities).extracting("name").containsExactly("Run Scripts",
                "Code Java", "Actividad 1");
    }
    @Test
    public void testGetActivity() {
        ResponseEntity<ActivityDto> response = this.restTemplate
                .getForEntity(URI.create("/v1/lms/activities/1"), ActivityDto.class);
        ActivityDto activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(activityDto).extracting("id").isEqualTo(1);
        assertThat(activityDto).extracting("name").isEqualTo("Run Scripts");
        assertThat(activityDto).extracting("points").isEqualTo(4);
    }

    @Test
    public void testGetActivity_NotFoundException() {
        ResponseEntity<Object> response = this.restTemplate
                .getForEntity(URI.create("/v1/lms/activities/19"), Object.class);
        System.out.println("Body: "+ response.getBody());
        Object errorDetails = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorDetails).extracting("message").isEqualTo("Not Found Activity by Id");
        System.out.println(response.toString());
    }

    @Test
    public void testInsertActivity() {
        ActivityDto activityDtoToInsert = ActivityDto.builder()
                .name("Activity 1").points(2).build();
        ResponseEntity<ActivityDto> response = this.restTemplate
                .postForEntity(URI.create("/v1/lms/activities"), activityDtoToInsert, ActivityDto.class);
        ActivityDto activityDtoInserted = response.getBody();
        String location = response.getHeaders().getLocation().toString();
        System.out.println("Location: "+location);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(location).contains("/v1/lms/activities/"+activityDtoInserted.getId());
        assertThat(activityDtoInserted).extracting("name").isEqualTo("Activity 1");
        assertThat(activityDtoInserted).extracting("points").isEqualTo(2);

        response = this.restTemplate
                .getForEntity(URI.create(location), ActivityDto.class);
        ActivityDto activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(activityDto).extracting("id").isEqualTo(activityDtoInserted.getId());
        assertThat(activityDto).extracting("name").isEqualTo(activityDtoInserted.getName());
        assertThat(activityDto).extracting("points").isEqualTo(activityDtoInserted.getPoints());
    }

    @Test
    public void postActivityWithExchange() {
        ActivityDto activityDtoToInsert = ActivityDto.builder()
                .name("Activity 1").points(2).build();
        HttpEntity<ActivityDto> request = new HttpEntity<>(activityDtoToInsert);
        ResponseEntity<ActivityDto> response = restTemplate
                .exchange(URI.create("/v1/lms/activities"), HttpMethod.POST, request, ActivityDto.class);
        ActivityDto activityDtoInserted = response.getBody();
        String location = response.getHeaders().getLocation().toString();
        System.out.println("Location: "+location);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(location).contains("/v1/lms/activities/"+activityDtoInserted.getId());
        assertThat(activityDtoInserted).extracting("name").isEqualTo("Activity 1");
        assertThat(activityDtoInserted).extracting("points").isEqualTo(2);

        response = this.restTemplate
                .getForEntity(URI.create(location), ActivityDto.class);
        ActivityDto activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(activityDto).extracting("id").isEqualTo(activityDtoInserted.getId());
        assertThat(activityDto).extracting("name").isEqualTo(activityDtoInserted.getName());
        assertThat(activityDto).extracting("points").isEqualTo(activityDtoInserted.getPoints());
    }

    @Test
    public void putActivity(){
        ActivityDto activityDtoToUpdate = ActivityDto.builder()
                .name("Activity To Update").points(9).build();
        restTemplate.put(URI.create("/v1/lms/activities/2"), activityDtoToUpdate);

        ResponseEntity<ActivityDto> response = this.restTemplate
                .getForEntity(URI.create("/v1/lms/activities/2"), ActivityDto.class);
        ActivityDto activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(activityDto).extracting("id").isEqualTo(2);
        assertThat(activityDto).extracting("name").isEqualTo(activityDtoToUpdate.getName());
        assertThat(activityDto).extracting("points").isEqualTo(activityDtoToUpdate.getPoints());
    }

    @Test
    public void putActivityWithExchange(){
        ActivityDto activityDtoToUpdate = ActivityDto.builder()
                .name("Activity To Update").points(9).build();
        HttpEntity<ActivityDto> request = new HttpEntity<>(activityDtoToUpdate);
        ResponseEntity<ActivityDto> response = restTemplate
                .exchange(URI.create("/v1/lms/activities/1"), HttpMethod.PUT, request, ActivityDto.class);
        ActivityDto activityDtoUpdated = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(activityDtoUpdated).extracting("id").isEqualTo(1);
        assertThat(activityDtoUpdated).extracting("name").isEqualTo("Activity To Update");
        assertThat(activityDtoUpdated).extracting("points").isEqualTo(9);

        response = this.restTemplate
                .getForEntity(URI.create("/v1/lms/activities/1"), ActivityDto.class);
        ActivityDto activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(activityDto).extracting("id").isEqualTo(1);
        assertThat(activityDto).extracting("name").isEqualTo(activityDtoToUpdate.getName());
        assertThat(activityDto).extracting("points").isEqualTo(activityDtoToUpdate.getPoints());
    }

    @Test
    public void putActivityWithExchange_NoExistsActivity(){
        ActivityDto activityDtoToUpdate = ActivityDto.builder()
                .name("Activity To Update").points(9).build();
        HttpEntity<ActivityDto> request = new HttpEntity<>(activityDtoToUpdate);
        ResponseEntity<ErrorDetails> response = restTemplate
                .exchange(URI.create("/v1/lms/activities/10"), HttpMethod.PUT, request, ErrorDetails.class);
        ErrorDetails errorDetails = response.getBody();
        System.out.println("Error Details: "+ errorDetails);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorDetails).extracting("message").isEqualTo("No se encontro una actividad con ese id");
        System.out.println(response.toString());
    }

    @Test
    @Rollback(true)
    public void deleteActivity(){
        restTemplate.delete(URI.create("/v1/lms/activities/2"));

        ResponseEntity<ActivityDto> response = this.restTemplate
                .getForEntity(URI.create("/v1/lms/activities/2"), ActivityDto.class);
        ActivityDto activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void deleteActivityWithExchange(){
        ActivityDto activityDtoToDelete = ActivityDto.builder()
                .name("Activity To Delete").points(9).build();
        HttpEntity<ActivityDto> request = new HttpEntity<>(activityDtoToDelete);
        ResponseEntity<ActivityDto> response = restTemplate
                .exchange(URI.create("/v1/lms/activities/2"), HttpMethod.DELETE, request, ActivityDto.class);
        ActivityDto activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

         response = this.restTemplate
                .getForEntity(URI.create("/v1/lms/activities/2"), ActivityDto.class);
         activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }



}
