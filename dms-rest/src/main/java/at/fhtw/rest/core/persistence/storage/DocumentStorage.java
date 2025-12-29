package at.fhtw.rest.core.persistence.storage;

import at.fhtw.rest.config.MinioProperties;
import at.fhtw.rest.core.exception.DocumentStorageException;
import io.minio.*;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentStorage {

    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    private final MinioClient client;

    private final MinioProperties properties;

    public InputStream load(String objectKey) {
        try {
            return loadFileFromBucket(objectKey);
        } catch (Exception ex) {
            log.error("Failed to load object '{}' from bucket '{}'", objectKey, properties.bucket(), ex);
            throw new DocumentStorageException("Failed to load object from MinIO", ex);
        }
    }

    private GetObjectResponse loadFileFromBucket(String objectKey)
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException,
            InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        return client.getObject(GetObjectArgs.builder()
                .bucket(properties.bucket())
                .object(objectKey)
                .build());
    }

    public String store(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String objectKey = generateObjectKey(getFileExtension(file));
            storeFileInBucket(file, objectKey, inputStream);
            log.info("Stored object '{}' in bucket '{}'", objectKey, properties.bucket());
            return objectKey;
        } catch (Exception ex) {
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

    private String getFileExtension(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            // todo: add error handling
            return null;
        }
        return contentType.split("/")[1];
    }

    private void storeFileInBucket(MultipartFile file, String objectKey, InputStream inputStream)
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException,
            InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        client.putObject(PutObjectArgs.builder()
                .bucket(properties.bucket())
                .object(objectKey)
                .contentType(file.getContentType())
                .stream(inputStream, file.getSize(), -1)
                .build()
        );
    }

    // todo: unused?
    public void delete(String objectKey) {
        try {
            deleteFileFromBucket(objectKey);
            log.info("Deleted object '{}' from bucket '{}'", objectKey, properties.bucket());
        } catch (Exception ex) {
            log.error("Failed to delete object '{}' from bucket '{}'", objectKey, properties.bucket(), ex);
            throw new DocumentStorageException("Failed to delete object from MinIO", ex);
        }
    }

    private void deleteFileFromBucket(String objectKey)
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException,
            InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        client.removeObject(RemoveObjectArgs.builder()
                .bucket(properties.bucket())
                .object(objectKey)
                .build());
    }
}
