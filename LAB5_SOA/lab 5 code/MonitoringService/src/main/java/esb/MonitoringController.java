package esb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

    @PostMapping(value = "/log", consumes = "text/plain")
    public ResponseEntity<?> log(@RequestBody String message) {
        System.out.println("🔵[MONITOR] " + message);
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
}

