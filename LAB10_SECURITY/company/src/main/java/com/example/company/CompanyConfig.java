package com.example.company;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class CompanyConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
