package net.runelite.client.plugins.potionFlipper;

import com.google.gson.*;
import okhttp3.*;
import java.io.IOException;

public class OsrsWikiApiHandler
{
    OkHttpClient httpClient = new OkHttpClient();
    private final String BASE_API_URL = "https://prices.runescape.wiki/api/v1/osrs/latest";

    public OsrsWikiItemData GetOsrsWikiResponse(int id) throws IOException {
        String url = BASE_API_URL + "?id=" + id;
        Request request = new Request.Builder().
                url(url).
                header("User-Agent", "PotionFlipper").
                build();

        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful())
        {
            String jsonString = response.body().string();
            return getOsrsWikiITemDataFromJson(jsonString);
        }
        else
        {
            return new OsrsWikiItemData();
        }
    }

    private OsrsWikiItemData getOsrsWikiITemDataFromJson(String jsonString)
    {
        Gson gson = new Gson();
        HttpResponse response = gson.fromJson(jsonString, HttpResponse.class);
        String osrsWikiITemDataString = response.getData().values().iterator().next().toString();
        return gson.fromJson(osrsWikiITemDataString, OsrsWikiItemData.class);
    }
}
