package com.example.contact;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.*;

@RestController
class ContactController {

    private static final Map<String, Map<String, String>> MOCK = new HashMap<>();

    static {
        MOCK.put("1", Map.of("id", "1", "name", "Lore", "phone", "+593-123-123"));
        MOCK.put("2", Map.of("id", "2", "name", "Leo", "phone", "+1-641-346-213"));
        MOCK.put("3", Map.of("id", "3", "name", "Carol", "phone", "+1-232-2312"));
    }

    @GetMapping
    public Collection<Map<String, String>> all() {
        return MOCK.values();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER')")
    @GetMapping("/phone")
    public ResponseEntity<String> getById() {
        System.out.println("Accessing contact phone data");
        return ResponseEntity.ok("641-555-1234");
    }
}
