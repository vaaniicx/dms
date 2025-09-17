package at.fhtw.rest;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import at.fhtw.rest.persistence.repository.JpaDocumentRepository;
import at.fhtw.rest.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DocumentControllerTest extends IntegrationTest {

    @Autowired
    private DocumentService service;

    @Autowired
    private DocumentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void uploadDocument_shouldReturnCreated() throws Exception {
        this.mockMvc.perform(multipart("/api/v1/documents")
                        .file(mockMultipartFile()))
                .andExpect(status().isCreated());
    }

    @Test
    void getDocuments_shouldReturnDocuments() throws Exception {
        persistDocument("first.txt");
        persistDocument("second.txt");

        this.mockMvc.perform(get("/api/v1/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("first.txt", "second.txt")));
    }

    @Test
    void getDocumentById_shouldReturnDocument() throws Exception {
        DocumentEntity savedDocument = persistDocument("unique.txt");

        this.mockMvc.perform(get("/api/v1/documents/{id}", savedDocument.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedDocument.getId().intValue())))
                .andExpect(jsonPath("$.title").value("unique.txt"));
    }

    private MockMultipartFile mockMultipartFile() {
        return new MockMultipartFile("file", "test.txt", "text/plain", "hello world!".getBytes());
    }

    private DocumentEntity persistDocument(String title) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setTitle(title);
        return this.service.save(documentEntity);
    }
}
