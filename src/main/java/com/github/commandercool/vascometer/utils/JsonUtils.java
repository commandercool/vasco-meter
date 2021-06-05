package com.github.commandercool.vascometer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.commandercool.vascometer.api.JsonMap;

import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonMap unmarshall(String json) {
        try {
            Map map = mapper.readValue(json, Map.class);
            return JsonMap.wrap(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
