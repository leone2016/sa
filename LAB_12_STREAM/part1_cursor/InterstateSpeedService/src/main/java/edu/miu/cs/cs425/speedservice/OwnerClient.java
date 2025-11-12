package edu.miu.cs.cs425.speedservice;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class OwnerClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OwnerClient.class);

    private final RestClient restClient;

    public OwnerClient(SpeedServiceProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.ownerServiceBaseUrl())
                .build();
    }

    public Optional<OwnerDetails> findOwner(String licencePlate) {
        try {
            OwnerDetails ownerDetails = restClient.get()
                    .uri("/owners/{licencePlate}", licencePlate)
                    .retrieve()
                    .body(OwnerDetails.class);
            return Optional.ofNullable(ownerDetails);
        } catch (HttpClientErrorException.NotFound notFound) {
            LOGGER.warn("Owner not found for licence plate {}", licencePlate);
            return Optional.empty();
        } catch (HttpClientErrorException clientError) {
            if (clientError.getStatusCode().is5xxServerError()) {
                LOGGER.error("Owner service error: {}", clientError.getMessage());
            } else if (!clientError.getStatusCode().equals(HttpStatusCode.valueOf(404))) {
                LOGGER.warn("Client error while retrieving owner {}: {}", licencePlate, clientError.getMessage());
            }
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.error("Failed to fetch owner details for {}: {}", licencePlate, e.getMessage());
            return Optional.empty();
        }
    }
}

