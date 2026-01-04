package at.fhtw.batch.mapper;

import at.fhtw.message.document.DocumentAccessedMessage;
import generated.FullAccessLogReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccessLogMapper {

    @Mapping(target = "accessDate", ignore = true)
    DocumentAccessedMessage mapToDocumentAccessedMessage(FullAccessLogReport.AccessLog accessLog);
}
