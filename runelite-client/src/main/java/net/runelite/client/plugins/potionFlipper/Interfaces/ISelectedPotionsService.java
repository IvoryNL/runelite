package net.runelite.client.plugins.potionFlipper.Interfaces;

import net.runelite.client.plugins.potionFlipper.ItemInfo;

import java.util.ArrayList;

public interface ISelectedPotionsService
{
    ArrayList<ItemInfo> getSelectedPotions();

    void setSelectedPotions(ArrayList<ItemInfo> selectedItems);
}
