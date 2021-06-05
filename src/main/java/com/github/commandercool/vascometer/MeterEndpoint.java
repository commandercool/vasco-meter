package com.github.commandercool.vascometer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.commandercool.vascometer.slack.SlackClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class MeterEndpoint {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ConcurrentHashMap<String, Integer> vascoMeter = new ConcurrentHashMap<>();

    @Autowired
    private SlackClient slackClient;

    @RequestMapping(method = RequestMethod.POST, path = "/", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> processTrigger(@RequestBody Map<String, Object> request) throws IOException, InterruptedException {
        System.out.println("Received request: " + mapper.writeValueAsString(request));
        Object challenge = request.get("challenge");
        if (challenge != null) {
            return ResponseEntity.ok((String) challenge);
        } else {
            Map event = (Map) request.get("event");
            Object reaction = event.get("reaction");
            if ("vasco".equals(reaction)) {
                String name = slackClient.getUserName(event.get("item_user").toString());
                Integer val = vascoMeter.getOrDefault(name, 0);
                Object type = event.get("type");
                if ("reaction_removed".equals(type)) {
                    vascoMeter.put(name, --val);
                } else {
                    vascoMeter.put(name, ++val);
                }
            }
        }
        return ResponseEntity.ok("");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/stats", produces = "application/json")
    public Map<String, Integer> callSlack() {
        return vascoMeter;
    }

}
