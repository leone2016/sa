package service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.time.Duration;

@RestController
public class ServiceOneController {
    @Autowired
    private ServiceTwoClient serviceTwoClient;
    @CircuitBreaker(name = "demoCircuitBreaker", fallbackMethod = "fallbackMethod")
    @RequestMapping("/text")
    public String getText() {
        String service2Text = serviceTwoClient.getText();
        return "Hello "+ service2Text;
    }
    private String fallbackMethod(Throwable throwable) {
        return "Hello World from fallbackMethod";
    }

    @FeignClient("ServiceTwo")
    interface ServiceTwoClient {
        @RequestMapping("/text")
        public String getText();
    }
}
