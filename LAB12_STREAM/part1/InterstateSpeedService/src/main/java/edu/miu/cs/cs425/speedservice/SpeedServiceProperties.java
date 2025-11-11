package edu.miu.cs.cs425.speedservice;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "owner.service")
public record SpeedServiceProperties(String baseUrl) {

    public String ownerServiceBaseUrl() {
        return baseUrl;
    }
}

