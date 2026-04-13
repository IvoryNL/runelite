package net.runelite.client.plugins.potionFlipper.Helpers;

public final class ProfitCalculatorHelper
{
    public static int calculateProfit3To4(int threeDosePrice, int fourDosePrice)
    {
        var priceThreeDoseToFourDoses = threeDosePrice / 3 * 4;
        var tax = 1.02f;

        return (int)((fourDosePrice / tax) - priceThreeDoseToFourDoses);
    }
}
