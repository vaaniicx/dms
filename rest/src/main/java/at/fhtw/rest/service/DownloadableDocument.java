package at.fhtw.rest.service;

import org.springframework.core.io.InputStreamResource;

public record DownloadableDocument(InputStreamResource stream, String fileName, String contentType) {
}
