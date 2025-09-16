package at.fhtw.rest.mapper;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import com.openapi.gen.springboot.dto.DocumentDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentDto toDocument(DocumentEntity entity);

    List<DocumentDto> toDocumentList(List<DocumentEntity> entities);
}
