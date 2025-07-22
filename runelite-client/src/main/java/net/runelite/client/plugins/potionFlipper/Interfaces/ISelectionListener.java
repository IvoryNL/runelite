package net.runelite.client.plugins.potionFlipper.Interfaces;

import java.util.ArrayList;

public interface ISelectionListener<T>
{
    void selectionDone(ArrayList<T> selectedItems);
}
