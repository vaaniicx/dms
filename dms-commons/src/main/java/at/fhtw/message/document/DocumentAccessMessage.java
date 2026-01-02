package at.fhtw.message.document;

import java.time.LocalDate;

public record DocumentAccessMessage(long documentId, String accessor, int accessCount, LocalDate accessDate) {
}

