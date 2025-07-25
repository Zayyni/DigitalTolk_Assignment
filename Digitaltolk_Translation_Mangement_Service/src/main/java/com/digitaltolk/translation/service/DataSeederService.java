package com.digitaltolk.translation.service;

import com.digitaltolk.translation.entity.Tag;
import com.digitaltolk.translation.entity.Translation;
import com.digitaltolk.translation.entity.User;
import com.digitaltolk.translation.repo.TagRepository;
import com.digitaltolk.translation.repo.TranslationRepository;
import com.digitaltolk.translation.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class DataSeederService {
	@Autowired
    private TranslationRepository translationRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final String[] LOCALES = {"en", "fr", "es", "de", "it", "pt", "ru", "zh", "ja", "ko"};
    private static final String[] CONTEXTS = {"mobile", "desktop", "web", "api", "email", "sms", "push"};
    private static final String[] CATEGORIES = {"ui", "validation", "error", "success", "navigation", "form", "button"};
    
    public void seedDatabase(int recordCount) {
        createDefaultUser();
        createTags();
        createTranslations(recordCount);
    }
    
    private void createDefaultUser() {
        if (!userRepository.existsByEmail("admin@digitaltolk.com")) {
            User admin = new User();
            admin.setEmail("admin@digitaltolk.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }
        
        if (!userRepository.existsByEmail("user@digitaltolk.com")) {
            User user = new User();
            user.setEmail("user@digitaltolk.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFullName("Test User");
            user.setRole(User.Role.USER);
            userRepository.save(user);
        }
    }
    
    private void createTags() {
        List<Tag> tags = new ArrayList<>();
        
        for (String context : CONTEXTS) {
            if (!tagRepository.existsByName(context)) {
                tags.add(new Tag(context, "Context tag for " + context));
            }
        }
        
        for (String category : CATEGORIES) {
            if (!tagRepository.existsByName(category)) {
                tags.add(new Tag(category, "Category tag for " + category));
            }
        }
        
        if (!tags.isEmpty()) {
            tagRepository.saveAll(tags);
        }
    }
    
    private void createTranslations(int recordCount) {
        List<Translation> translations = new ArrayList<>();
        Set<Tag> allTags = new HashSet<>(tagRepository.findAll());
        List<Tag> tagList = new ArrayList<>(allTags);
        
        int batchSize = 1000;
        int processedCount = 0;
        
        for (int i = 1; i <= recordCount; i++) {
            for (String locale : LOCALES) {
                String key = generateTranslationKey(i);
                String content = generateTranslationContent(key, locale, i);
                
                if (!translationRepository.existsByTranslationKeyAndLocale(key, locale)) {
                    Translation translation = new Translation(key, locale, content);
                    
                    // Add 1-3 random tags
                    int tagCount = ThreadLocalRandom.current().nextInt(1, 4);
                    Set<Tag> randomTags = getRandomTags(tagList, tagCount);
                    translation.setTags(randomTags);
                    
                    translations.add(translation);
                    processedCount++;
                }
                
                // Batch insert for performance
                if (translations.size() >= batchSize) {
                    translationRepository.saveAll(translations);
                    translations.clear();
                    System.out.println("Processed " + processedCount + " translations...");
                }
            }
        }
        
        // Save remaining translations
        if (!translations.isEmpty()) {
            translationRepository.saveAll(translations);
            System.out.println("Completed! Total translations created: " + processedCount);
        }
    }
    
    private String generateTranslationKey(int index) {
        String[] prefixes = {"app", "common", "auth", "user", "product", "order", "payment", "notification"};
        String[] suffixes = {"title", "label", "message", "error", "success", "warning", "info", "placeholder"};
        
        String prefix = prefixes[ThreadLocalRandom.current().nextInt(prefixes.length)];
        String suffix = suffixes[ThreadLocalRandom.current().nextInt(suffixes.length)];
        
        return prefix + "." + suffix + "." + index;
    }
    
    private String generateTranslationContent(String key, String locale, int index) {
        Map<String, String[]> localeContent = Map.of(
            "en", new String[]{"Welcome", "Login", "Error occurred", "Success", "Loading", "Save", "Cancel", "Delete"},
            "fr", new String[]{"Bienvenue", "Connexion", "Erreur survenue", "Succès", "Chargement", "Sauvegarder", "Annuler", "Supprimer"},
            "es", new String[]{"Bienvenido", "Iniciar sesión", "Error ocurrido", "Éxito", "Cargando", "Guardar", "Cancelar", "Eliminar"},
            "de", new String[]{"Willkommen", "Anmelden", "Fehler aufgetreten", "Erfolg", "Laden", "Speichern", "Abbrechen", "Löschen"},
            "it", new String[]{"Benvenuto", "Accedi", "Errore verificato", "Successo", "Caricamento", "Salva", "Annulla", "Elimina"}
        );
        
        String[] words = localeContent.getOrDefault(locale, localeContent.get("en"));
        String baseWord = words[ThreadLocalRandom.current().nextInt(words.length)];
        
        return baseWord + " " + index + " [" + key + "]";
    }
    
    private Set<Tag> getRandomTags(List<Tag> tagList, int count) {
        Set<Tag> randomTags = new HashSet<>();
        for (int i = 0; i < count && !tagList.isEmpty(); i++) {
            Tag randomTag = tagList.get(ThreadLocalRandom.current().nextInt(tagList.size()));
            randomTags.add(randomTag);
        }
        return randomTags;
    }

}
