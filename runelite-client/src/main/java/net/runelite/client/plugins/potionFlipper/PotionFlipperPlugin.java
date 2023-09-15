package net.runelite.client.plugins.potionFlipper;

import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.image.BufferedImage;

@PluginDescriptor(
    name = "PotionFlipper",
    description = "Enable the Potion Flipper panel",
    tags = {"panel"},
    loadWhenOutdated = true
)
public class PotionFlipperPlugin extends Plugin
{
    @Inject
    private ClientToolbar clientToolbar;
    private NavigationButton navigationButton;
    private PotionFlipperPanel potionFlipperPanel;

    @Provides
    PotionFlipperConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(PotionFlipperConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        potionFlipperPanel = new PotionFlipperPanel();

        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "herblore.png");

        navigationButton = NavigationButton.builder()
            .tooltip("Potion Flipper")
            .icon(icon)
            .priority(11)
            .panel(potionFlipperPanel)
            .build();

        clientToolbar.addNavigation(navigationButton);
    }

    @Override
    protected void shutDown() throws Exception
    {
        clientToolbar.removeNavigation(navigationButton);
    }
}
