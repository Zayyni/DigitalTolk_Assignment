package com.digitaltolk.translation.repo;

import com.digitaltolk.translation.entity.Translation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface TranslationRepository extends JpaRepository<Translation, Long>{
Optional<Translation> findByTranslationKeyAndLocale(String translationKey, String locale);
    
    List<Translation> findByLocale(String locale);
    
    @Query("SELECT t FROM Translation t WHERE t.locale = :locale AND t.updatedAt > :lastUpdate")
    List<Translation> findByLocaleAndUpdatedAfter(@Param("locale") String locale, 
                                                  @Param("lastUpdate") LocalDateTime lastUpdate);
    
    @Query("SELECT t FROM Translation t JOIN t.tags tag WHERE tag.name IN :tagNames")
    Page<Translation> findByTagNames(@Param("tagNames") List<String> tagNames, Pageable pageable);
    
    @Query("SELECT t FROM Translation t WHERE " +
           "(:key IS NULL OR t.translationKey LIKE %:key%) AND " +
           "(:locale IS NULL OR t.locale = :locale) AND " +
           "(:content IS NULL OR LOWER(t.content) LIKE LOWER(CONCAT('%', :content, '%')))")
    Page<Translation> searchTranslations(@Param("key") String key,
                                       @Param("locale") String locale,
                                       @Param("content") String content,
                                       Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Translation t WHERE t.locale = :locale")
    long countByLocale(@Param("locale") String locale);
    
    @Query("SELECT DISTINCT t.locale FROM Translation t")
    List<String> findAllLocales();
    
    boolean existsByTranslationKeyAndLocale(String translationKey, String locale);

}
