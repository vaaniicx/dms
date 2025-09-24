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

    private String fileType;
    private String fileName;
    private Long fileSize;

    private Long docPageCount;
    private String docTitle;
    private String docAuthor;
    private Instant docCreatedAt;
    private Instant docUpdatedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant insertedAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
