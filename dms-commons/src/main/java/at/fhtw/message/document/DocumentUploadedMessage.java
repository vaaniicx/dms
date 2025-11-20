package at.fhtw.message.document;

public record DocumentUploadedMessage(long documentId, String objectKey) {
}
