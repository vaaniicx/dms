package at.fhtw.ocr.storage;

import java.nio.file.Path;

public interface StorageService {
    Path downloadToTempFile(String objectKey);
}

