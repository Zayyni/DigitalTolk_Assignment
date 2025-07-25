package com.digitaltolk.translation.controller;

import com.digitaltolk.translation.dto.TranslationDto;
import com.digitaltolk.translation.service.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/translations")
@Tag(name = "Translation Management", description = "APIs for managing translations")
public class TranslationController {
	
	@Autowired
    private TranslationService translationService;
    
    @PostMapping
    @Operation(summary = "Create a new translation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TranslationDto> createTranslation(@Valid @RequestBody TranslationDto dto) {
        TranslationDto created = translationService.createTranslation(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing translation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TranslationDto> updateTranslation(
            @PathVariable Long id, 
            @Valid @RequestBody TranslationDto dto) {
        TranslationDto updated = translationService.updateTranslation(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get translation by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TranslationDto> getTranslation(@PathVariable Long id) {
        TranslationDto translation = translationService.getTranslation(id);
        return ResponseEntity.ok(translation);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a translation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        translationService.deleteTranslation(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search translations with filters")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Page<TranslationDto>> searchTranslations(
            @Parameter(description = "Translation key filter") @RequestParam(required = false) String key,
            @Parameter(description = "Locale filter") @RequestParam(required = false) String locale,
            @Parameter(description = "Content filter") @RequestParam(required = false) String content,
            @Parameter(description = "Tag filters") @RequestParam(required = false) List<String> tags,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "updatedAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<TranslationDto> results = translationService.searchTranslations(
            key, locale, content, tags, page, size, sortBy, sortDir);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/export/{locale}")
    @Operation(summary = "Export translations for a specific locale as JSON")
    public ResponseEntity<Map<String, String>> exportTranslations(
            @PathVariable String locale,
            @Parameter(description = "Only return translations updated after this timestamp")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdate) {
        
        Map<String, String> translations = translationService.exportTranslations(locale, lastUpdate);
        return ResponseEntity.ok(translations);
    }
    
    @GetMapping("/locales")
    @Operation(summary = "Get all available locales")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<String>> getAllLocales() {
        List<String> locales = translationService.getAllLocales();
        return ResponseEntity.ok(locales);
    }

}
