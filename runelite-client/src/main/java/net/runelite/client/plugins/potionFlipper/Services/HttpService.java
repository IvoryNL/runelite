package net.runelite.client.plugins.potionFlipper.Services;

import net.runelite.client.plugins.potionFlipper.Interfaces.IHttpService;
import okhttp3.*;

import javax.inject.Inject;
import java.io.IOException;

public class HttpService implements IHttpService
{
    private final OkHttpClient httpClient;

    @Inject
    public HttpService(OkHttpClient httpClient)
    {
        this.httpClient = httpClient;
    }

    @Override
    public String getAll(String url) throws IOException
    {
        return getResponse(url);
    }

    @Override
    public String getById(int id, String url) throws IOException
    {
        return getResponse(url + "?id=" + id);
    }

    private String getResponse(String url) throws IOException
    {
        Request request = new Request.Builder().
                url(url).
                header("User-Agent", "PotionFlipper").
                build();

        Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        if (response.body() == null) throw new IOException("Response body is null");

        return  response.body().string();
    }
}
