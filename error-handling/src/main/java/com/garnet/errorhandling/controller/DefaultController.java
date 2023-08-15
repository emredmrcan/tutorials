package com.garnet.errorhandling.controller;

import com.garnet.errorhandling.exception.CriticalBusinessException;
import com.garnet.errorhandling.exception.ResourceNotFoundException;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/tutorial")
public class DefaultController implements ErrorController {

    @GetMapping("/{id}")
    public ResponseEntity<?> getResource(@PathVariable @PositiveOrZero Long id) {
        // Let's simulate some scenarios
        if (id == 0) {
            throw new ResourceNotFoundException("Resource with ID " + id + " not found");
        } else if (id == 1) {
            throw new CriticalBusinessException("An error occurred while retrieving the resource");
        }

        return ResponseEntity.ok("Resource found with ID " + id);
    }

}
