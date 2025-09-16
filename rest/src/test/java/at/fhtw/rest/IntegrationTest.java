package at.fhtw.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"integration"})
@SpringBootTest(classes = {RestApplication.class})
class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
}
