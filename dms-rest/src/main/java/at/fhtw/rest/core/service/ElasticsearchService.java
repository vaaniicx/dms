package at.fhtw.rest.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ObjectMapper objectMapper;

    @Value("${elasticsearch.url:http://localhost:9200}")
    private String elasticsearchUrl;

    @Value("${elasticsearch.index:documents}")
    private String indexName;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<Long> searchDocumentIds(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        try {
            String baseUrl = (elasticsearchUrl != null && !elasticsearchUrl.isBlank())
                ? elasticsearchUrl
                : "http://localhost:9200";
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }

            String resolvedIndex = (indexName != null && !indexName.isBlank()) ? indexName : "documents";

            URI uri = URI.create(String.format("%s/%s/_search", baseUrl, resolvedIndex));

            String body = """
                {
                  "query": {
                    "multi_match": {
                      "query": "%s",
                      "fields": ["content"]
                    }
                  },
                  "size": 100
                }
                """.formatted(query.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("Failed to search documents in Elasticsearch. Status: {}, Body: {}",
                    response.statusCode(), response.body());
                throw new IllegalStateException("Failed to search documents in Elasticsearch");
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode hitsNode = root.path("hits").path("hits");

            List<Long> ids = new ArrayList<>();
            for (JsonNode hit : hitsNode) {
                JsonNode source = hit.path("_source");
                long id = source.path("documentId").asLong(-1);
                if (id > 0) {
                    ids.add(id);
                }
            }

            log.info("Elasticsearch search for '{}' returned {} documents", query, ids.size());
            return ids;
        } catch (Exception e) {
            log.error("Error while searching documents in Elasticsearch", e);
            throw new RuntimeException("Elasticsearch search failed", e);
        }
    }
}
