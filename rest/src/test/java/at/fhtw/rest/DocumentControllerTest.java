package at.fhtw.rest;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DocumentControllerTest extends IntegrationTest {

    @Autowired
    private DocumentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void uploadDocument_shouldReturnBadRequest_whenNoFileProvided() throws Exception {
        this.mockMvc.perform(multipart("/api/v1/documents"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadDocument_shouldReturnUnsupportedMediaType_whenNonPdfProvided() throws Exception {
        MockMultipartFile txt = new MockMultipartFile(
                "file",
                "note.txt",
                "text/plain",
                "hello world!".getBytes(StandardCharsets.UTF_8)
        );

        this.mockMvc.perform(multipart("/api/v1/documents").file(txt))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void getDocuments_shouldReturnDocuments() throws Exception {
        persistDocument("first.txt");
        persistDocument("second.txt");

        this.mockMvc.perform(get("/api/v1/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].fileName", containsInAnyOrder("first.txt", "second.txt")));
    }

    @Test
    void getDocumentById_shouldReturnDocument() throws Exception {
        DocumentEntity savedDocument = persistDocument("unique.txt");

        this.mockMvc.perform(get("/api/v1/documents/{id}", savedDocument.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedDocument.getId().intValue())))
                .andExpect(jsonPath("$.fileName").value("unique.txt"));
    }

    private DocumentEntity persistDocument(String filename) {
        DocumentEntity e = DocumentEntity.builder()
                .fileType("application/pdf")
                .fileName(filename)
                .fileSize(42L)
                .build();
        return repository.save(e);
    }
}