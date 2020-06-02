package com.lms.microservices.activitymicro.exception;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles the exceptions.
   * @param ex The Exception thrown
   * @param request The WebRequest that comes from the exception
   * @return
   */
  @ExceptionHandler(ActivityNotFoundException.class)
  public ResponseEntity<ErrorDetails> activityNotFoundExceptionHandle(
      RuntimeException ex, WebRequest request) {
    Map<String, String[]> parameterMap = request.getParameterMap();
    request.getContextPath();
    Set<String> strings = parameterMap.keySet();
    System.out.println("Size set: " + strings.size());
    strings.stream().forEach(System.out::println);

    ErrorDetails errorDetails = ErrorDetails.builder().timestamp(LocalDateTime.now())
        .message(ex.getMessage()).details(request.getDescription(false)).build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
  }
}
