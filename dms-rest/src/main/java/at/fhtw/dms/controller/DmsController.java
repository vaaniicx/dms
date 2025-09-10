package at.fhtw.dms.controller;

import at.fhtw.dms.openapi.api.ApiDms;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DmsController implements ApiDms {

    @Override
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Alive.");
    }
}
