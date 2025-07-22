package net.runelite.client.plugins.potionFlipper.Interfaces;

import java.io.IOException;
import java.util.List;

public interface IHttpService
{
    String getAll(String url) throws IOException;
    String getById(int id, String url) throws IOException;
}
