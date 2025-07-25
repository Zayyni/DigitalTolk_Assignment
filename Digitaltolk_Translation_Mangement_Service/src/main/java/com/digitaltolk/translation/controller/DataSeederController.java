package com.digitaltolk.translation.controller;
import com.digitaltolk.translation.service.DataSeederService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Administrative APIs")
@PreAuthorize("hasRole('ADMIN')")
public class DataSeederController {
	@Autowired
    private DataSeederService dataSeederService;
    
    @PostMapping("/seed/{count}")
    @Operation(summary = "Seed database with test data")
    public ResponseEntity<String> seedDatabase(@PathVariable int count) {
        if (count > 100000) {
            return ResponseEntity.badRequest().body("Maximum allowed count is 100,000");
        }
        
        dataSeederService.seedDatabase(count);
        return ResponseEntity.ok("Database seeded with " + count + " records per locale");
    }

}
