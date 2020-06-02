package com.lms.microservices.activitymicro.repository;

import com.lms.microservices.activitymicro.entity.Activity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActivityRepository extends CrudRepository<Activity, Integer> {

  List<Activity> findByIdIn(List<Integer> ids);

}
