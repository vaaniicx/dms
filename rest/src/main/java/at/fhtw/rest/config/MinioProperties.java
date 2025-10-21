package at.fhtw.rest.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "minio")
public record MinioProperties(
        @NotBlank
        String endpoint,

        @NotBlank
        String accessKey,

        @NotBlank
        String secretKey,

        @NotBlank
        String bucket
) {
}
