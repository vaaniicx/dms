package at.fhtw.rest.storage;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class MinioStorageService implements ObjectStorageService {
    private final MinioClient client;
    private final String bucket;

    @Override
    public void upload(String objectKey, InputStream data, long size, String contentType) {
        try {
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(data, size, -1)
                            .contentType(contentType)
                            .build()
            );
            log.info("Uploaded object '{}' to bucket '{}'", objectKey, bucket);
        } catch (Exception e) {
            log.error("Failed to upload object '{}' to MinIO", objectKey, e);
            throw new RuntimeException("Failed to upload to MinIO", e);
        }
    }
}

