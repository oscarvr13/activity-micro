package com.lms.microservices.activitymicro.services;

import com.lms.microservices.activitymicro.dto.ActivityDto;
import com.lms.microservices.activitymicro.entity.Activity;
import com.lms.microservices.activitymicro.exception.ActivityNotFoundException;
import com.lms.microservices.activitymicro.repository.ActivityRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    @Test
    public void testGetAllActivities_Basic() {
        when(activityRepository.findAll())
                .thenReturn(Arrays.asList(
                        Activity.builder().id(1).name("Java Code")
                                        .points(5).build(),
                                Activity.builder().id(2).name("Run Scripts")
                                        .points(8).build(),
                                Activity.builder().id(3).name("Activity 3")
                                        .points(9).build()
                        ));
        ResponseEntity<List<ActivityDto>> response = activityService.getAllActivities();
        List<ActivityDto> listActivties = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listActivties).hasSize(3);
        assertThat(listActivties).extracting("id")
                .containsExactly(1, 2, 3);
        assertThat(listActivties).extracting("name")
                .containsExactly("Java Code", "Run Scripts", "Activity 3");
    }

    @Test
    public void testGetAllActivities_EmptyList() {
        when(activityRepository.findAll())
                .thenReturn(Collections.EMPTY_LIST);
        ResponseEntity<List<ActivityDto>> response = activityService.getAllActivities();
        List<ActivityDto> listActivties = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listActivties).hasSize(0);
    }

    @Test
    public void testGetActivity_Basic() {
        when(activityRepository.findById(3))
                .thenReturn(Optional.of(Activity.builder().id(3).name("Activity 3")
                        .points(9).build()));
        ResponseEntity<ActivityDto> response = activityService.getActivity(3);
        ActivityDto activityDto = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(activityDto).extracting("id", "name", "points")
                .containsExactlyInAnyOrder(3, "Activity 3", 9);
    }

    @Test
    public void testGetActivity_NoExistActivity() {
        exceptionRule.expect(ResponseStatusException.class);
        exceptionRule.expectMessage("Not Found Activity by Id");
        when(activityRepository.findById(4))
                .thenReturn(Optional.empty());
        ResponseEntity<ActivityDto> response = activityService.getActivity(4);
    }

    @Test
    public void testInsertActivity_Basic() {
        ActivityDto activityDtoToInsert = ActivityDto.builder()
                .name("Activity 1").points(2).build();
        Activity activityToSave = Activity.builder().name(activityDtoToInsert.getName())
                .points(activityDtoToInsert.getPoints()).build();
        when(activityRepository.save(activityToSave))
                .thenReturn(Activity.builder().id(1).name(activityToSave.getName())
                        .points(activityToSave.getPoints()).build());
        ActivityDto activityDtoSaved = activityService.insertActivity(activityDtoToInsert);
        assertThat(activityDtoSaved).extracting("id", "name", "points")
                .containsExactlyInAnyOrder(1, "Activity 1", 2);
    }

    @Test
    public void testUpdate_Basic() {
        int activityId = 1;
        ActivityDto activityDtoToUpdate = ActivityDto.builder()
                .name("Activity 1").points(2).build();
        Activity activityToUpdate = Activity.builder()
                .id(activityId)
                .name(activityDtoToUpdate.getName())
                .points(activityDtoToUpdate.getPoints()).build();
        when(activityRepository.findById(activityId))
                .thenReturn(Optional.of(activityToUpdate));
        when(activityRepository.save(activityToUpdate))
                .thenReturn(activityToUpdate);
        ActivityDto activityDto = activityService.updateActivity(activityId, activityDtoToUpdate);
        assertThat(activityDto).extracting("id", "name", "points")
                .containsExactlyInAnyOrder(1, "Activity 1", 2);
    }

    @Test
    public void testUpdateActivity_NoExistActivity() {
        exceptionRule.expect(ActivityNotFoundException.class);
        exceptionRule.expectMessage("No se encontro una actividad con ese id");
        int activityId = 4;
        ActivityDto activityDtoToUpdate = ActivityDto.builder()
                .name("Activity 1").points(2).build();
         when(activityRepository.findById(activityId))
                .thenReturn(Optional.empty());
        ActivityDto response = activityService.updateActivity(activityId, activityDtoToUpdate);
    }

    @Test
    public void testDelete_Basic() {
        int activityId = 1;
        when(activityRepository.existsById(activityId))
                .thenReturn(true);
        doNothing().when(activityRepository).deleteById(activityId);
        boolean deletedActivity = activityService.deleteActivity(activityId);
        assertThat(deletedActivity).isTrue();
    }

    @Test
    public void NotFoundActivity() {
        exceptionRule.expect(ActivityNotFoundException.class);
        exceptionRule.expectMessage("No se encontro una actividad con ese id");
        int activityId = 1;
        when(activityRepository.existsById(activityId))
                .thenReturn(false);
        boolean deletedActivity = activityService.deleteActivity(activityId);
        assertThat(deletedActivity).isTrue();
    }








}
