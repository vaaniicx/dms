package at.fhtw.rest.messaging.dto;

public record DocumentMessage(Long documentId, String objectKey) {
    public static DocumentMessage from(Long documentId, String objectKey) {
        return new DocumentMessage(documentId, objectKey);
    }
}
