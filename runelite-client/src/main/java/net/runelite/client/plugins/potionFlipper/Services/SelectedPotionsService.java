package net.runelite.client.plugins.potionFlipper.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.potionFlipper.Interfaces.ISelectedPotionsService;
import net.runelite.client.plugins.potionFlipper.ItemInfo;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SelectedPotionsService implements ISelectedPotionsService
{
    public static final String CONFIG_GROUP = "potionFlipper";
    public static final String CONFIG_KEY_SELECTED_POTIONS = "selectedPotions";

    private final ConfigManager configManager;
    private final Gson gson;

    @Inject
    public SelectedPotionsService(ConfigManager configManager, Gson gson)
    {
        this.configManager = configManager;
        this.gson = gson;
    }

    @Override
    public ArrayList<ItemInfo> getSelectedPotions()
    {
        Type itemListType = new TypeToken<ArrayList<ItemInfo>>(){}.getType();
        String json = configManager.getConfiguration(CONFIG_GROUP, CONFIG_KEY_SELECTED_POTIONS, String.class);

        return (json != null && !json.trim().isEmpty()) ? gson.fromJson(json, itemListType) : new ArrayList<>();
    }

    @Override
    public void setSelectedPotions(ArrayList<ItemInfo> selectedItems)
    {
        var json = gson.toJson(selectedItems);
        configManager.setConfiguration(CONFIG_GROUP, CONFIG_KEY_SELECTED_POTIONS, json);
    }
}
