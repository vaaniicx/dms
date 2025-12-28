package at.fhtw.rest.mapper.document;

import at.fhtw.rest.core.persistence.entity.DocumentFile;
import at.fhtw.rest.mapper.DateMapper;
import com.openapi.gen.springboot.dto.DocumentFileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface DocumentFileMapper {

    DocumentFileResponse mapToResponse(DocumentFile documentFile);
}
