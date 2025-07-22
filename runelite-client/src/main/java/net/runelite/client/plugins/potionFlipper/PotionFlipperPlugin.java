package net.runelite.client.plugins.potionFlipper;

import com.google.gson.Gson;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.potionFlipper.Interfaces.IHttpService;
import net.runelite.client.plugins.potionFlipper.Interfaces.ISelectedPotionsService;
import net.runelite.client.plugins.potionFlipper.Interfaces.PotionFlipperConfig;
import net.runelite.client.plugins.potionFlipper.Services.HttpService;
import net.runelite.client.plugins.potionFlipper.Services.SelectedPotionsService;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;

@PluginDescriptor(
        name = "PotionFlipper",
        description = "Enable the Potion Flipper panel",
        tags = {"panel"}
)
@Slf4j
public class PotionFlipperPlugin extends Plugin
{
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private PotionFlipperPanel potionFlipperPanel;
    @Inject
    private SelectPotionsPanel selectPotionsPanel;
    @Inject
    private Gson gson;

    private NavigationButton navPotionFlipper;

    @Override
    protected void startUp() throws Exception
    {
        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "herblore.png");

        navPotionFlipper = NavigationButton.builder()
                .tooltip("Potion Flipper")
                .icon(icon)
                .priority(11)
                .panel(potionFlipperPanel)
                .build();

        clientToolbar.addNavigation(navPotionFlipper);
    }

    @Override
    protected void shutDown() throws Exception
    {
        clientToolbar.removeNavigation(navPotionFlipper);
    }

    @Provides
    @Singleton
    IHttpService provideHttpService(OkHttpClient httpClient)
    {
        return new HttpService(httpClient);
    }

    @Provides
    PotionFlipperConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(PotionFlipperConfig.class);
    }

    @Provides
    ISelectedPotionsService provideSelectedPotionsService(ConfigManager configManager, Gson gson)
    {
        return new SelectedPotionsService(configManager, gson);
    }
}
