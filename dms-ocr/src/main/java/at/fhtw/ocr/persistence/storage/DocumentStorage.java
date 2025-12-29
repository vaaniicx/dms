package at.fhtw.ocr.persistence.storage;

import at.fhtw.ocr.config.MinioProperties;
import at.fhtw.ocr.exception.DocumentStorageException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentStorage {
    private final MinioClient client;
    private final MinioProperties properties;

    public InputStream load(String objectKey) {
        try {
            return client.getObject(GetObjectArgs.builder()
                    .bucket(properties.bucket())
                    .object(objectKey)
                    .build());
        } catch (Exception ex) {
            log.error("Failed to load object '{}' from bucket '{}'", objectKey, properties.bucket(), ex);
            throw new DocumentStorageException("Failed to load object from MinIO", ex);
        }
    }
}
