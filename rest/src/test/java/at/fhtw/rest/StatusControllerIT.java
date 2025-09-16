package at.fhtw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StatusControllerIT extends IntegrationTest {

    @Test
    void getStatus_shouldReturnOk() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/v1/status"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Alive.");
    }
}
