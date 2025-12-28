package at.fhtw.rest.core.util;

import org.springframework.core.io.InputStreamResource;

public record DownloadableDocument(InputStreamResource stream, String fileName, String contentType) {
}
