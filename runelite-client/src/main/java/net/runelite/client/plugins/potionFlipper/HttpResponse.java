package net.runelite.client.plugins.potionFlipper;

import com.google.gson.*;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private Map<String, JsonObject> data = new HashMap<>();

    public void setData(String key, JsonObject value) {
        data.put(key, value);
    }

    public Map<String, JsonObject> getData() {
        return data;
    }
}
