package at.fhtw.rest.mapper.document;

import at.fhtw.rest.core.persistence.entity.Document;
import com.openapi.gen.springboot.dto.DocumentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DocumentFileMapper.class, DocumentStatusHistoryMapper.class})
public interface DocumentMapper {

    @Mapping(target = "file", source = "documentFile")
    @Mapping(target = "status", source = "statusHistory")
    DocumentResponse mapToResponse(Document document);
}
