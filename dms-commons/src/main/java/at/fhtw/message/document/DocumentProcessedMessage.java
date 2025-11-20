package at.fhtw.message.document;

public record DocumentProcessedMessage(long documentId, String summarizedText) {
}
