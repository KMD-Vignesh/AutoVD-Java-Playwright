package com.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Properties file utility — reads key/value pairs from .properties files.
 */
public class PropertiesUtil {

    private final Properties properties;
    private final String filePath;

    public PropertiesUtil(String filePath) {
        this.filePath = filePath;
        this.properties = new Properties();
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("[WARN] Could not load properties from: " + filePath);
            e.printStackTrace();
        }
    }

    public String getValue(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public String getValue(String propertyName, String defaultValue) {
        return properties.getProperty(propertyName, defaultValue);
    }

    public Set<Object> getKeys() {
        return properties.keySet();
    }

    public HashMap<String, String> getMap() {
        HashMap<String, String> map = new HashMap<>();
        for (Object key : properties.keySet()) {
            map.put(key.toString(), properties.getProperty(key.toString()));
        }
        return map;
    }

    public int getIntValue(String propertyName) {
        return Integer.parseInt(properties.getProperty(propertyName));
    }

    public int getIntValue(String propertyName, int defaultValue) {
        String val = properties.getProperty(propertyName);
        return val != null ? Integer.parseInt(val) : defaultValue;
    }

    public boolean getBooleanValue(String propertyName) {
        return Boolean.parseBoolean(properties.getProperty(propertyName));
    }
}
