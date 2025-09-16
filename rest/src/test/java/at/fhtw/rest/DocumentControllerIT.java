package at.fhtw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DocumentControllerIT extends IntegrationTest {

    @Test
    void uploadDocument_shouldReturnOk() throws Exception {
        this.mockMvc.perform(multipart("/api/v1/documents")
                        .file(mockMultipartFile()))
                .andExpect(status().isOk());
    }

    private MockMultipartFile mockMultipartFile() {
        return new MockMultipartFile("file", "test.txt", "text/plain", "hello world!".getBytes());
    }
}
