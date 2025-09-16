package at.fhtw.rest.mapper;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import com.openapi.gen.springboot.dto.Document;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document toDocument(DocumentEntity entity);

    List<Document> toDocumentList(List<DocumentEntity> entities);
}
