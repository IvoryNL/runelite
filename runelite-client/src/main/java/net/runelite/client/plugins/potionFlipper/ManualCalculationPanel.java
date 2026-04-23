package net.runelite.client.plugins.potionFlipper;

import net.runelite.client.plugins.potionFlipper.Helpers.JElementHelper;
import net.runelite.client.plugins.potionFlipper.Helpers.ProfitCalculatorHelper;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;

public class ManualCalculationPanel extends PluginPanel
{
    private JLabel calculationResult3to4Label;
    private JLabel calculationResult4to3Label;
    private JTextField threeDoseTextField;
    private JTextField fourDoseTextField;
    private JTextField quantityTextField;

    public ManualCalculationPanel()
    {
        super();

        buildPanel();
    }

    private void buildPanel()
    {
        JPanel layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
        layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        layoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        float FONT_SIZE = 13f;
        var threeDoseLabel = JElementHelper.createLabel("Three Dose Price:", FONT_SIZE);
        threeDoseLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, threeDoseLabel.getPreferredSize().height));
        var fourDoseLabel = JElementHelper.createLabel("Four Dose Price:", FONT_SIZE);
        fourDoseLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, fourDoseLabel.getPreferredSize().height));
        var calculation3to4Label = JElementHelper.createLabel("Profit Calculation 3 to 4 doses:", FONT_SIZE);
        calculation3to4Label.setMaximumSize(new Dimension(Integer.MAX_VALUE, calculation3to4Label.getPreferredSize().height));
        var calculation4to3Label = JElementHelper.createLabel("Profit Calculation 4 to 3 doses:", FONT_SIZE);
        calculation4to3Label.setMaximumSize(new Dimension(Integer.MAX_VALUE, calculation4to3Label.getPreferredSize().height));
        calculationResult3to4Label = JElementHelper.createLabel("...", FONT_SIZE);
        calculationResult3to4Label.setMaximumSize(new Dimension(Integer.MAX_VALUE, calculationResult3to4Label.getPreferredSize().height));
        calculationResult4to3Label = JElementHelper.createLabel("...", FONT_SIZE);
        calculationResult4to3Label.setMaximumSize(new Dimension(Integer.MAX_VALUE, calculationResult4to3Label.getPreferredSize().height));
        var quantityLabel = JElementHelper.createLabel("Quantity:", FONT_SIZE);

        threeDoseTextField = JElementHelper.createTextField();
        threeDoseTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, threeDoseTextField.getPreferredSize().height));
        fourDoseTextField = JElementHelper.createTextField();
        fourDoseTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fourDoseTextField.getPreferredSize().height));
        quantityTextField = JElementHelper.createTextField();
        quantityTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fourDoseTextField.getPreferredSize().height));

        var calculateButton = JElementHelper.createButton("Calculate", "Calculate profit from three dose to four dose potion.");
        calculateButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, calculateButton.getPreferredSize().height));
        calculateButton.addActionListener(e -> calculate());

        layoutPanel.add(threeDoseLabel);
        layoutPanel.add(threeDoseTextField);
        layoutPanel.add(fourDoseLabel);
        layoutPanel.add(fourDoseTextField);
        layoutPanel.add(quantityLabel);
        layoutPanel.add(quantityTextField);
        layoutPanel.add(calculation3to4Label);
        layoutPanel.add(calculationResult3to4Label);
        layoutPanel.add(calculation4to3Label);
        layoutPanel.add(calculationResult4to3Label);
        layoutPanel.add(calculateButton);

        add(layoutPanel);
    }

    private void calculate()
    {
        var threeDosePrice = threeDoseTextField.getText();
        var fourDosePrice = fourDoseTextField.getText();
        var quantity = quantityTextField.getText();

        if (threeDosePrice.isEmpty() || fourDosePrice.isEmpty())
        {
            var errorMessage = "Please enter both prices.";
            updateCalculationResult(errorMessage, errorMessage);
            return;
        }

        if (quantity.isEmpty())
        {
            var errorMessage = "Please enter a quantity.";
            updateCalculationResult(errorMessage, errorMessage);
            return;
        }

        if (!threeDosePrice.matches("\\d+") || !fourDosePrice.matches("\\d+") || !quantity.matches("\\d+"))
        {
            var errorMessage = "Please enter valid numeric values.";
            updateCalculationResult(errorMessage, errorMessage);
            return;
        }

        var result3to4 = ProfitCalculatorHelper.calculateProfit3To4(
                Integer.parseInt(threeDosePrice),
                Integer.parseInt(fourDosePrice));
        var result4to3 = ProfitCalculatorHelper.calculateProfit4To3(
                Integer.parseInt(fourDosePrice),
                Integer.parseInt(threeDosePrice));
        var quantityValue = Integer.parseInt(quantity);
        var sumTotal3to4 = result3to4 * quantityValue;
        var sumTotal4to3 = result4to3 * quantityValue;

        updateCalculationResult(String.valueOf(sumTotal3to4), String.valueOf(sumTotal4to3));
    }

    private void updateCalculationResult(String result3to4, String result4to3)
    {
        calculationResult3to4Label.setText(result3to4);
        calculationResult4to3Label.setText(result4to3);
        repaint();
    }
}
