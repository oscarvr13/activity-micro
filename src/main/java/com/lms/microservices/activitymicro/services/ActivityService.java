package com.lms.microservices.activitymicro.services;

import com.google.common.collect.Lists;
import com.lms.microservices.activitymicro.dto.ActivityDto;
import com.lms.microservices.activitymicro.entity.Activity;
import com.lms.microservices.activitymicro.exception.ActivityNotFoundException;
import com.lms.microservices.activitymicro.repository.ActivityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@RequiredArgsConstructor
@Service
public class ActivityService {

  /**
   * Received the ActivityRepository bean.
   */
  private final ActivityRepository activityRepository;

  /**
   * Get all the activities.
   * @return Response Entity with an activities
   *         list wrapped inside
   */
  public ResponseEntity<List<ActivityDto>> getAllActivities() {
    List<ActivityDto> listActivitiesDto =
        Lists.newArrayList(activityRepository.findAll())
            .stream().map(activity -> ActivityDto.builder().id(activity.getId())
            .name(activity.getName()).points(activity.getPoints()).build())
            .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK)
        // .contentType(MediaType.APPLICATION_JSON)
        //  .contentType(MediaType.APPLICATION_ATOM_XML)
        .body(listActivitiesDto);
  }

  /**
   * Get only one activity.
   * @param activityId The activity Id to retrieve.
   * @return A Response Entity that wraps the ActivityDto object.
   */
  public ResponseEntity<ActivityDto> getActivity(int activityId) {
    return activityRepository.findById(activityId)
        .map(activity -> {
          ActivityDto responseActivity = ActivityDto.builder()
              .id(activity.getId()).name(activity.getName())
              .points(activity.getPoints()).build();
          return ResponseEntity.status(HttpStatus.OK)
              // .contentType(MediaType.APPLICATION_JSON)
              // .contentType(MediaType.APPLICATION_ATOM_XML)
              .body(responseActivity);
        })
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Not Found Activity by Id"));
    //      .orElseThrow(() -> new ActivityNotFoundException("Not Activity Found"));
  }

  /**
   * Insert one activity.
   * @param activityDto The activity to insert.
   * @return the activity inserted.
   */
  public ActivityDto insertActivity(ActivityDto activityDto) {
    Activity activity = Activity.builder().name(activityDto.getName())
        .points(activityDto.getPoints()).build();
    final Activity activitySaved = activityRepository.save(activity);
    return ActivityDto.builder().id(activitySaved.getId())
        .name(activitySaved.getName()).points(activitySaved.getPoints()).build();
    //return ResponseEntity.created(location).body(
    //        ActivityDto.builder().id(activitySaved.getId()).name(activitySaved.getName())
    //        .points(activitySaved.getPoints()).build());
        /*
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", )
                .body(
                ActivityDto.builder().id(activitySaved.getId()).name(activitySaved.getName())
                .points(activitySaved.getPoints()).build());
         */
  }

  /**
   * Update and activity.
   * @param activityId The activity Id to update.
   * @param activityDto The activity values replace the old ones.
   * @return
   */
  public ActivityDto updateActivity(int activityId, ActivityDto activityDto) {
    return activityRepository.findById(activityId).map(activity -> {
      activity.setName(activityDto.getName());
      activity.setPoints(activityDto.getPoints());
      Activity activityUpdated = activityRepository.save(activity);
      return ActivityDto.builder().id(activityUpdated.getId())
          .name(activityUpdated.getName())
          .points(activityUpdated.getPoints()).build();
    })
        .orElseThrow(() -> new ActivityNotFoundException(
            "No se encontro una actividad con ese id"));
    //.orElse(insertIfNotExists(activityToUpdate));
  }

  /**
   * Delete and Activity.
   * @param activityId The activity Id to delete.
   * @return true if it was removed otherwise false.
   */
  public boolean deleteActivity(int activityId) {
    boolean existsActivity = activityRepository.existsById(activityId);
    if (existsActivity) {
      activityRepository.deleteById(activityId);
      return true;
    } else {
      throw new ActivityNotFoundException("No se encontro una actividad con ese id");
    }
  }


  public ResponseEntity<List<ActivityDto>> getAllActivitiesById(List<Integer> idList) {
    System.out.println("Entra aqui Service getAllActivitiesById");

    try {
      Thread.sleep(18000);
    }catch(InterruptedException ex){
      System.out.println("Exception"+ ex);
    }

    List<ActivityDto> listActivitiesDto =
        activityRepository.findByIdIn(idList)
            .stream().map(activity -> ActivityDto.builder().id(activity.getId())
            .name(activity.getName()).points(activity.getPoints()).build())
            .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK)
        .body(listActivitiesDto);
  }
}
