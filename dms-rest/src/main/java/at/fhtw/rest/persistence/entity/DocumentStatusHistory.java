package at.fhtw.rest.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
}
