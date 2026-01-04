package at.fhtw.batch.tasks;

import at.fhtw.batch.message.publisher.MessagePublisher;
import at.fhtw.batch.util.XmlReader;
import generated.FullAccessLogReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final MessagePublisher messagePublisher;

    @Value("${batch.access.logs.directory:test}")
    private String accessLogsDirectory;

    @Value("${batch.access.logs.pattern:test}")
    private Pattern accessLogsFilePattern;

    private static List<FullAccessLogReport> getFullAccessLogReports(List<Path> accessLogPaths) {
        return accessLogPaths
                .stream()
                .map(path -> XmlReader.unmarshal(FullAccessLogReport.class, path))
                .toList();
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void processAccessLogs() {
        log.info("Processing access logs");
        try (Stream<Path> accessLogsFilePaths = Files.walk(Paths.get(accessLogsDirectory))) {
            List<Path> accessLogPaths = getAccessLogPaths(accessLogsFilePaths);
            processFullAccessLogReport(getFullAccessLogReports(accessLogPaths));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processFullAccessLogReport(List<FullAccessLogReport> fullAccessLogReports) {
        for (FullAccessLogReport fullAccessLogReport : fullAccessLogReports) {
            XMLGregorianCalendar accessDate = fullAccessLogReport.getAccess();
            sendDocumentAccessedMessageForAccessLogs(fullAccessLogReport, accessDate);
        }
    }

    private void sendDocumentAccessedMessageForAccessLogs(FullAccessLogReport fullAccessLogReport, XMLGregorianCalendar accessDate) {
        for (FullAccessLogReport.AccessLog accessLog : fullAccessLogReport.getAccessLog()) {
            log.info("Publish message for access log: {}", accessLog);
            messagePublisher.publishDocumentAccessed(getLocalDateFromCalendar(accessDate), accessLog);
        }
    }

    private List<Path> getAccessLogPaths(Stream<Path> accessLogsFilePaths) {
        return accessLogsFilePaths
                .filter(Files::isRegularFile)
                .filter(path -> accessLogsFilePattern.matcher(path.getFileName().toString()).matches())
                .toList();
    }

    private LocalDate getLocalDateFromCalendar(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }
}
