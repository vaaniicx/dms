package at.fhtw.ocr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

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

    public void indexDocument(long documentId, String content) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("documentId", documentId);
            payload.put("content", content);

            String body = objectMapper.writeValueAsString(payload);

            String baseUrl = (elasticsearchUrl != null && !elasticsearchUrl.isBlank())
                ? elasticsearchUrl
                : "http://localhost:9200";
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }

            String resolvedIndex = (indexName != null && !indexName.isBlank()) ? indexName : "documents";

            URI uri = URI.create(String.format("%s/%s/_doc/%d", baseUrl, resolvedIndex, documentId));

            HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("Failed to index document {} in Elasticsearch. Status: {}, Body: {}",
                    documentId, response.statusCode(), response.body());
                throw new IllegalStateException("Failed to index document in Elasticsearch");
            }

            log.info("Indexed document {} in Elasticsearch index '{}'", documentId, resolvedIndex);
        } catch (Exception e) {
            log.error("Error while indexing document {} in Elasticsearch", documentId, e);
            throw new RuntimeException("Elasticsearch indexing failed", e);
        }
    }
}
