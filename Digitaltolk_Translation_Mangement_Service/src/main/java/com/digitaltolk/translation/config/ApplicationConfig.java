package com.digitaltolk.translation.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableCaching
@EnableJpaAuditing
public class ApplicationConfig {
	@Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("translations", "translationExport");
    }

}
