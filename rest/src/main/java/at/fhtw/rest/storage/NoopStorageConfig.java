package at.fhtw.rest.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class NoopStorageConfig {
    @Bean
    @ConditionalOnMissingBean(ObjectStorageService.class)
    public ObjectStorageService objectStorageService() {
        return new ObjectStorageService() {
            @Override
            public void upload(String objectKey, InputStream data, long size, String contentType) {
            }
        };
    }
}

