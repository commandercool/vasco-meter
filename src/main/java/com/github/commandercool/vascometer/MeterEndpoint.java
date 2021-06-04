package com.github.commandercool.vascometer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeterEndpoint {

    @RequestMapping(method = RequestMethod.POST, path = "/", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> processTrigger(@RequestBody Map<String, String> request) {
        String challenge = request.get("challenge");
        return ResponseEntity.ok(challenge);
    }

}
