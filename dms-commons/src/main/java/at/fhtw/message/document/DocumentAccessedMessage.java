package at.fhtw.message.document;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DocumentAccessedMessage {

    private String accessor;

    private long documentId;

    private int accessCount;

    private LocalDate accessDate;
}

