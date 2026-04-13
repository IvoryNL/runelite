package net.runelite.client.plugins.potionFlipper.Interfaces;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("potionFlipper")
public interface PotionFlipperConfig extends Config
{
    @ConfigItem(
            keyName     = "selectedPotions",
            name        = "Selected Potions",
            description = "An ArrayList of potion IDs and names",
            position    = 1
    )
    default String selectedPotions()
    {
        return "";
    }
}
