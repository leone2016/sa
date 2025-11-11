package edu.miu.cs.cs425.speedservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SpeedServiceProperties.class)
public class SpeedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeedServiceApplication.class, args);
    }

}
