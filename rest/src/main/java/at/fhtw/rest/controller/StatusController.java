package at.fhtw.rest.controller;

import com.openapi.gen.springboot.api.StatusApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class StatusController implements StatusApi {

    @Override
    public ResponseEntity<String> getStatus() {
        log.debug("GET /status");
        return ResponseEntity.ok("Alive.");
    }
}
