package com.github.commandercool.vascometer;

import com.github.commandercool.vascometer.api.JsonMap;
import com.github.commandercool.vascometer.slack.SlackClient;
import com.github.commandercool.vascometer.utils.JsonUtils;
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

    private static final ConcurrentHashMap<String, Integer> vascoMeter = new ConcurrentHashMap<>();

    @Autowired
    private SlackClient slackClient;

    @RequestMapping(method = RequestMethod.POST, path = "/", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> processTrigger(@RequestBody String json) throws IOException, InterruptedException {
        System.out.println("Received request: " + json);
        JsonMap request = JsonUtils.unmarshall(json);
        String challenge = request.getString("challenge");
        if (challenge != null) {
            return ResponseEntity.ok(challenge);
        } else {
            JsonMap event = request.getMap("event");
            String reaction = event.getString("reaction");
            if ("vasco".equals(reaction)) {
                String name = slackClient.getUserName(event.getString("item_user"));
                Integer val = vascoMeter.getOrDefault(name, 0);
                String type = event.getString("type");
                if ("reaction_removed".equals(type) && val > 0) {
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
