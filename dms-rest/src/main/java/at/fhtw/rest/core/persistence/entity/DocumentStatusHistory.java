package at.fhtw.rest.core.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "document_status_history")
public class DocumentStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @Column(name = "change_date")
    private LocalDateTime changeDate;

    public DocumentStatusHistory(DocumentStatus status) {
        this.status = status;
        this.changeDate = LocalDateTime.now();
    }
}
