package at.fhtw.ocr.ocr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OcrProcessor {
    private final OcrProperties props;
    private final CommandRunner runner;

    public String extractText(Path pdfFile) {
        try {
            Path tiff = Files.createTempFile("pages-", ".tiff");
            List<String> gsCmd = List.of(
                    "gs", "-dNOPAUSE", "-dBATCH", "-sDEVICE=tiffg4",
                    "-r" + props.dpi(),
                    "-sOutputFile=" + tiff.toAbsolutePath(),
                    pdfFile.toAbsolutePath().toString()
            );
            runner.runAndCapture(gsCmd);

            List<String> tessCmd = List.of(
                    "tesseract",
                    tiff.toAbsolutePath().toString(),
                    "stdout",
                    "-l", props.language()
            );
            String text = runner.runAndCapture(tessCmd);
            try { Files.deleteIfExists(tiff); } catch (Exception ignore) {}
            return text == null ? "" : text.trim();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("OCR failed", e);
        }
    }
}

