package at.fhtw.rest.persistence.storage;

import at.fhtw.rest.config.MinioProperties;
import at.fhtw.rest.exception.DocumentStorageException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentStorage {
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private final MinioClient client;
    private final MinioProperties properties;

    public String store(MultipartFile file, String fileExtension) {
        String objectKey = generateObjectKey(fileExtension);
        try (InputStream inputStream = file.getInputStream()) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(properties.bucket())
                    .object(objectKey)
                    .contentType(file.getContentType())
                    .stream(inputStream, file.getSize(), -1)
                    .build()
            );
            log.info("Stored object '{}' ({} bytes) in bucket '{}'", objectKey, file.getSize(), properties.bucket());
            return objectKey;
        } catch (Exception ex) {
            log.error("Failed to store object '{}' in bucket '{}'", objectKey, properties.bucket(), ex);
            throw new DocumentStorageException("Failed to store object in MinIO", ex);
        }
    }

    private static String generateObjectKey(String fileExtension) {
        UUID uuid = UUID.randomUUID();
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * 2)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());
        return String.format("documents/%s.%s", ENCODER.encodeToString(buffer.array()), fileExtension);
    }
}
