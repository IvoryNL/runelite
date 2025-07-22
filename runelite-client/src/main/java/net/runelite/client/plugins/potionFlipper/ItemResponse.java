package net.runelite.client.plugins.potionFlipper;

import com.google.gson.JsonObject;

import java.util.Map;

public class ItemResponse
{
    public Map<Integer, JsonObject> data;

    public Map<Integer, JsonObject> getData()
    {
        return data;
    }
}
