package at.fhtw.rest.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Builder
@Entity
@Table(name = "documents")
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objectKey;

    private String fileType;

    private String fileName;

    private Long fileSize;

    private Long docPageCount;

    private String docTitle;

    private String docAuthor;

    private Instant docCreatedAt;

    private Instant docUpdatedAt;

    @Builder.Default
    private boolean uploaded = false;

    @Builder.Default
    private boolean scanned = false;

    @Builder.Default
    private boolean summarized = false;

    @Builder.Default
    private boolean indexed = false;

    @Lob
    private String summary;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant insertedAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
