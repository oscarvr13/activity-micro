package com.lms.microservices.activitymicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ActivityMicroApplication {

  //Adding some changes for the branch branchToNewDev

  /**
   * Main method.
   * @param args Args received.
   */
  public static void main(String[] args) {
    SpringApplication.run(ActivityMicroApplication.class, args);
  }
}
