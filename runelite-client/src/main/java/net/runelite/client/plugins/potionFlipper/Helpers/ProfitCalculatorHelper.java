package net.runelite.client.plugins.potionFlipper.Helpers;

import net.runelite.client.plugins.potionFlipper.ItemPrice;

public final class ProfitCalculatorHelper
{
    public static int calculateProfit3To4(int threeDosePrice, int fourDosePrice)
    {
        var priceThreeDoseTimesToFourDoses = threeDosePrice / 3 * 4;
        var tax = 1.02f;

        return (int)((fourDosePrice - priceThreeDoseTimesToFourDoses) / tax);
    }
}
