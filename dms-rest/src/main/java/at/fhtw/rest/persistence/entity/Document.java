package at.fhtw.rest.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Lob
    private String summary;

    @OneToOne
    @JoinColumn(name = "document_id")
    private DocumentFile documentFile;

    @OneToMany
    @JoinColumn(name = "document_id")
    private List<DocumentStatusHistory> statusHistory;
}
