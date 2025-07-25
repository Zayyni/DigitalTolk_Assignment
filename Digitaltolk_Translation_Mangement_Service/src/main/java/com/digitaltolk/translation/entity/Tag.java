package com.digitaltolk.translation.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags", indexes = {
    @Index(name = "idx_tag_name", columnList = "name", unique = true)
})
public class Tag {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Tag name cannot be blank")
    @Size(max = 100, message = "Tag name must not exceed 100 characters")
    @Column(nullable = false, unique = true)
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Translation> translations = new HashSet<>();
    
    // Constructors
    public Tag() {}
    
    public Tag(String name) {
        this.name = name;
    }
    
    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Set<Translation> getTranslations() { return translations; }
    public void setTranslations(Set<Translation> translations) { this.translations = translations; }
}


