package com.framework.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON data utility — reads test data from JSON files.
 */
public class JsonUtil {

    private final Object jsonObject;
    private final String resourceLocation;
    private final Gson gson;

    public JsonUtil(String jsonFilePath) {
        JSONParser parser = new JSONParser();
        try {
            jsonObject = parser.parse(new FileReader(new File(jsonFilePath)));
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to parse JSON file: " + jsonFilePath, e);
        }
        this.resourceLocation = jsonFilePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Deserialize the root JSON object into a POJO.
     */
    public <T> T getPojo(Class<T> clazz) {
        String jsonStr = gson.toJson(jsonObject);
        return gson.fromJson(jsonStr, clazz);
    }

    /**
     * Get the raw JSONObject.
     */
    @SuppressWarnings("unchecked")
    public JSONObject getObject() {
        return (JSONObject) jsonObject;
    }

    /**
     * Get a value by key.
     */
    public String getValue(String key) {
        return getObject().get(key).toString();
    }

    /**
     * Get all keys.
     */
    public Object[] getKeys() {
        return getObject().keySet().toArray();
    }

    /**
     * Get all key-value pairs as a Map.
     */
    public Map<String, String> getAllValues() {
        Map<String, String> map = new java.util.TreeMap<>();
        for (Object key : getKeys()) {
            map.put(key.toString(), getValue(key.toString()));
        }
        return map;
    }

    /**
     * Pretty-print the entire JSON content.
     */
    public String getPrettyPrint() {
        return gson.toJson(jsonObject);
    }
}
