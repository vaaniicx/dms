package at.fhtw.ocr.ocr;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class OcrProcessorTest {

    @Test
    void extractText_returnsTrimmedText_fromTesseractOutput() throws Exception {
        OcrProperties props = new OcrProperties("eng", 300);
        CommandRunner runner = Mockito.mock(CommandRunner.class);

        when(runner.runAndCapture(anyList()))
                .thenReturn("")
                .thenReturn(" Hello World \n\n");

        OcrProcessor ocr = new OcrProcessor(props, runner);

        var tmp = Files.createTempFile("test-doc", ".pdf");
        try {
            String text = ocr.extractText(tmp);
            assertThat(text).isEqualTo("Hello World");
        } finally {
            Files.deleteIfExists(tmp);
        }
    }
}

