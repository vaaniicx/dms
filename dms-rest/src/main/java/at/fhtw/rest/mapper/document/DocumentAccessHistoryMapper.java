package at.fhtw.rest.mapper.document;

import at.fhtw.rest.core.persistence.entity.DocumentAccessHistory;
import at.fhtw.rest.mapper.DateMapper;
import com.openapi.gen.springboot.dto.DocumentAccessHistoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface DocumentAccessHistoryMapper {

    DocumentAccessHistoryResponse mapToResponse(DocumentAccessHistory documentAccessHistory);
}

