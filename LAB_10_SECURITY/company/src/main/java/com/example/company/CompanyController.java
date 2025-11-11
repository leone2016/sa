package com.example.company;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
class CompanyController {

    private final RestTemplate restTemplate;

    public CompanyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // public product data accessible to everyone
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/productdata")
    public Map<String, Object> productData() {
        return Map.of(
                "company", "Company XYZ",
                "products", new String[]{"Payroll System", "Employee Portal", "Analytics"}
        );
    }


    // employee contact data - only EMPLOYEE and MANAGER can access (enforced by @PreAuthorize)
    @GetMapping("/phone")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER')")
    public ResponseEntity<String> employeeContact(@AuthenticationPrincipal Jwt jwt) {
        String url = "http://localhost:8082";
        String token = jwt.getTokenValue();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        });
        String response = restTemplate.getForObject(url + "/phone", String.class);
        return ResponseEntity.ok(response);
    }

    // salary data - only MANAGER can access
    @GetMapping("/salary")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> employeeSalary(@AuthenticationPrincipal Jwt jwt) {
        String url = "http://localhost:8083";
        String token = jwt.getTokenValue();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        });
        String response = restTemplate.getForObject(url + "/salary", String.class);
        return ResponseEntity.ok(response);
    }
}
