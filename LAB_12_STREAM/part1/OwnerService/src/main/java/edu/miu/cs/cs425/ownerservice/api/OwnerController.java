package edu.miu.cs.cs425.ownerservice.api;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.miu.cs.cs425.ownerservice.model.Owner;
import edu.miu.cs.cs425.ownerservice.service.OwnerCatalog;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerCatalog ownerCatalog;

    public OwnerController(OwnerCatalog ownerCatalog) {
        this.ownerCatalog = ownerCatalog;
    }

    @GetMapping("/{licencePlate}")
    public Owner getOwner(@PathVariable String licencePlate) {
        return ownerCatalog.findOwnerByLicencePlate(licencePlate)
                .orElseThrow(() -> new NoSuchElementException("Unknown licence plate " + licencePlate));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}

