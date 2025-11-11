package service;



import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ServiceOneController {

    @Autowired
    ServiceTwoClient serviceTwoClient;

    @RequestMapping("/text")
    public String getText() {
        String service2Text = serviceTwoClient.getText();
        return "Hello "+ service2Text;
    }

    @FeignClient("ServiceTwo")
    interface ServiceTwoClient {
        @RequestMapping("/text")
        public String getText();
    }
}
