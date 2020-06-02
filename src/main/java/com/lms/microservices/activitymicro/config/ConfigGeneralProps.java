package com.lms.microservices.activitymicro.config;

import com.lms.microservices.activitymicro.dto.PropertiesDto;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "general-props")
@Setter
public class ConfigGeneralProps {

  private int prop1;

  private String prop2;

  private boolean prop3;

  @Bean
  public PropertiesDto create() {
    System.out.println("Initializes the bean properties");
    return PropertiesDto.builder().prop1(prop1).prop2(prop2).prop3(prop3).build();
  }


}
