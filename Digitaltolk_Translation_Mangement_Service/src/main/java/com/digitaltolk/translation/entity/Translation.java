package com.digitaltolk.translation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "translations", indexes = {
	    @Index(name = "idx_translation_key", columnList = "translationKey"),
	    @Index(name = "idx_locale", columnList = "locale"),
	    @Index(name = "idx_key_locale", columnList = "translationKey, locale", unique = true),
	    @Index(name = "idx_updated_at", columnList = "updatedAt")
	})
public class Translation {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Translation key cannot be blank")
    @Size(max = 255, message = "Translation key must not exceed 255 characters")
    @Column(name = "translation_key", nullable = false)
    private String translationKey;
    
    @NotBlank(message = "Locale cannot be blank")
    @Size(max = 10, message = "Locale must not exceed 10 characters")
    @Column(nullable = false)
    private String locale;
    
    @NotBlank(message = "Content cannot be blank")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "translation_tags",
        joinColumns = @JoinColumn(name = "translation_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"),
        indexes = {
            @Index(name = "idx_translation_tags_translation", columnList = "translation_id"),
            @Index(name = "idx_translation_tags_tag", columnList = "tag_id")
        }
    )
    private Set<Tag> tags = new HashSet<>();
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
    
    // Constructors
    public Translation() {}
    
    public Translation(String translationKey, String locale, String content) {
        this.translationKey = translationKey;
        this.locale = locale;
        this.content = content;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTranslationKey() { return translationKey; }
    public void setTranslationKey(String translationKey) { this.translationKey = translationKey; }
    
    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getTranslations().add(this);
    }
    
    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getTranslations().remove(this);
    }

}
