package at.fhtw.rest.messaging.dto;

import java.util.Arrays;

public record DocumentMessage(Long documentId, byte[] content) {
    public static DocumentMessage from(Long documentId, byte[] content) {
        return new DocumentMessage(documentId, Arrays.copyOf(content, content.length));
    }
}
