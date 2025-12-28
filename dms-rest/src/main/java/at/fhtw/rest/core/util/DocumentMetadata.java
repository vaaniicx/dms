package at.fhtw.rest.core.util;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentMetadata {

    private String title;

    private String author;

    private int pageCount;

    private LocalDateTime creationDate;

    private LocalDateTime modificationDate;
}
