package com.github.commandercool.vascometer.slack;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class SlackClient {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String token = System.getenv("SLACK_TOKEN");

    private final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("https://slack.com/api/users.info");

    public String getUserName(String usedId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = factory.builder()
                .queryParam("user", usedId).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        HashMap userInfo = mapper.readValue(response.body(), HashMap.class);
        Object name = ((Map) userInfo.get("user")).get("name");
        return name.toString();
    }

}
