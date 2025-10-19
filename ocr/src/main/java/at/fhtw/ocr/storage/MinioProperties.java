package at.fhtw.ocr.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ocr.minio")
public record MinioProperties(String endpoint, String accessKey, String secretKey, String bucket) {}
