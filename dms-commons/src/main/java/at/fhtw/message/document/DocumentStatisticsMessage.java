package at.fhtw.message.document;

import java.time.LocalDateTime;

public record DocumentStatisticsMessage(
        long documentId,
        LocalDateTime accessDate,
        int accessCount,
        String accessor
) {
}

