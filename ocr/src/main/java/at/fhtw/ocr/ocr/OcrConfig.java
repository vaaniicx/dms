package at.fhtw.ocr.ocr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OcrConfig {

    @Bean
    public CommandRunner commandRunner() {
        return new CommandRunner();
    }

    @Bean
    public OcrProcessor ocrProcessor(OcrProperties props, CommandRunner runner) {
        return new OcrProcessor(props, runner);
    }
}

