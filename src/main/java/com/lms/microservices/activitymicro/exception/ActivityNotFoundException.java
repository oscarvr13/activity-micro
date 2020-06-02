package com.lms.microservices.activitymicro.exception;

/**
 *This class is my custom exception.
 *
 */
public class ActivityNotFoundException extends RuntimeException {
  public ActivityNotFoundException(String message) {
    super(message);
  }
}
