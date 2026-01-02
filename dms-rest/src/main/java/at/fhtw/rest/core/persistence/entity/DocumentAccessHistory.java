package at.fhtw.rest.core.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "document_access_history")
public class DocumentAccessHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_date", nullable = false)
    private LocalDateTime accessDate;

    @Column(name = "access_count", nullable = false)
    private Integer accessCount;

    @Column(name = "accessor", nullable = false)
    private String accessor;

    @Column(name = "document_id")
    private Long documentId;
}

