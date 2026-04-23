package net.runelite.client.plugins.potionFlipper.Helpers;

public final class ProfitCalculatorHelper
{
    private final static float tax = 0.98f;

    public static int calculateProfit3To4(int threeDosePriceHigh, int fourDosePriceLow)
    {
        var cost = threeDosePriceHigh / 3f * 4;
        var sellPrice = fourDosePriceLow * tax;

        return (int)(sellPrice - cost);
    }

    public static int calculateProfit4To3(int fourDosePriceHigh, int threeDosePriceLow)
    {
        var cost = fourDosePriceHigh / 4f * 3;
        var sellPrice = threeDosePriceLow  * tax ;

        return (int)(sellPrice - cost);
    }
}
