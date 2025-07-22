package net.runelite.client.plugins.potionFlipper;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ItemPriceResponse
{
    private Map<String, JsonObject> data = new HashMap<>();

    public Map<String, JsonObject> getData()
    {
        return data;
    }
}
