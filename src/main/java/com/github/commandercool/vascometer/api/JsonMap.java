package com.github.commandercool.vascometer.api;

import java.util.Map;

public class JsonMap {

    private final Map<String, Object> map;

    private JsonMap(Map<String, Object> map) {
        this.map = map;
    }

    public static JsonMap wrap(Map<String, Object> map) {
        return new JsonMap(map);
    }

    public JsonMap getMap(String key) {
        return JsonMap.wrap((Map) map.get(key));
    }

    public String getString(String key) {
        return (String) map.get(key);
    }

}
