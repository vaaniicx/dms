package at.fhtw.ocr.storage;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class MinioStorageService implements StorageService {
    private final MinioClient client;
    private final String bucket;

    @Override
    public Path downloadToTempFile(String objectKey) {
        try {
            Path tmp = Files.createTempFile("document-", "-" + objectKey.replaceAll("[^a-zA-Z0-9._-]", "_"));
            try (InputStream in = client.getObject(GetObjectArgs.builder().bucket(bucket).object(objectKey).build())) {
                Files.copy(in, tmp, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            return tmp;
        } catch (Exception e) {
            throw new RuntimeException("Failed to download object from MinIO", e);
        }
    }
}

