package at.fhtw.rest.mapper;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import com.openapi.gen.springboot.dto.DocumentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring", uses = DocumentFormatter.class)
public interface DocumentMapper {
    @Mapping(source = "fileSize", target = "fileSize", qualifiedByName = "formatFileSize")
    @Mapping(source = "fileSize", target = "fileSizeUnit", qualifiedByName = "formatFileUnit")
    @Mapping(source = "fileType", target = "fileExtension", qualifiedByName = "formatFileExtension")
    DocumentDto toDocument(DocumentEntity entity);

    List<DocumentDto> toDocumentList(List<DocumentEntity> entities);

    default OffsetDateTime map(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime odt) {
        return odt == null ? null : odt.toInstant();
    }

    default JsonNullable<String> map(String value) {
        return value == null ? JsonNullable.<String>undefined() : JsonNullable.of(value);
    }
}

