package com.example.salary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.*;

@RestController
class SalaryController {

    private static final Map<String, Map<String, String>> MOCK = new HashMap<>();

    static {
        MOCK.put("1", Map.of("id", "1", "name", "Lore", "salary", "70000"));
        MOCK.put("2", Map.of("id", "2", "name", "Leo", "salary", "40000"));
        MOCK.put("3", Map.of("id", "3", "name", "Carol", "salary", "30000"));
    }

    @GetMapping
    public Map<String, Map<String, String>> all() {
        return MOCK;
    }

    @GetMapping("/salary")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> getById() {
        return ResponseEntity.ok("A LOT");
    }
}