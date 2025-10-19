package at.fhtw.rest.storage;

import java.io.InputStream;

public interface ObjectStorageService {
    void upload(String objectKey, InputStream data, long size, String contentType);
}

