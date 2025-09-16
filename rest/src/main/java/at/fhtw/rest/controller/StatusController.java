package at.fhtw.rest.controller;

import com.openapi.gen.springboot.api.StatusApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StatusController implements StatusApi {

    @Override
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Alive.");
    }
}
