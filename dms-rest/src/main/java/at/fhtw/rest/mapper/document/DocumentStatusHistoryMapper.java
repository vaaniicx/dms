package at.fhtw.rest.mapper.document;

import at.fhtw.rest.core.persistence.entity.DocumentStatusHistory;
import at.fhtw.rest.mapper.DateMapper;
import com.openapi.gen.springboot.dto.DocumentStatusHistoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface DocumentStatusHistoryMapper {

    DocumentStatusHistoryResponse mapToResponse(DocumentStatusHistory documentStatusHistory);
}
