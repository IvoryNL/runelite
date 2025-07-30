package net.runelite.client.plugins.potionFlipper;

import net.runelite.client.plugins.potionFlipper.Helpers.JElementHelper;
import net.runelite.client.plugins.potionFlipper.Helpers.ProfitCalculatorHelper;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;

public class ManualCalculationPanel extends PluginPanel
{
    private final float FONT_SIZE = 13f;

    private JLabel calculationResultLabel;
    private JTextField threeDoseTextField;
    private JTextField fourDoseTextField;

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

        var calculationLabel = JElementHelper.createLabel("Profit Calculation:", FONT_SIZE);
        calculationLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, calculationLabel.getPreferredSize().height));
        calculationResultLabel = JElementHelper.createLabel("...", FONT_SIZE);
        calculationResultLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, calculationResultLabel.getPreferredSize().height));

        var calculateButton = JElementHelper.createButton("Calculate", "Calculate profit from three dose to four dose potion.");
        calculateButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, calculateButton.getPreferredSize().height));
        calculateButton.addActionListener(e -> calculate());

        var threeDoseLabel = JElementHelper.createLabel("Three Dose Price:", FONT_SIZE);
        threeDoseLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, threeDoseLabel.getPreferredSize().height));
        var fourDoseLabel = JElementHelper.createLabel("Four Dose Price:", FONT_SIZE);
        fourDoseLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, fourDoseLabel.getPreferredSize().height));

        threeDoseTextField = JElementHelper.createTextField();
        threeDoseTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, threeDoseTextField.getPreferredSize().height));
        fourDoseTextField = JElementHelper.createTextField();
        fourDoseTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fourDoseTextField.getPreferredSize().height));

        layoutPanel.add(threeDoseLabel);
        layoutPanel.add(threeDoseTextField);
        layoutPanel.add(fourDoseLabel);
        layoutPanel.add(fourDoseTextField);
        layoutPanel.add(calculationLabel);
        layoutPanel.add(calculationResultLabel);
        layoutPanel.add(calculateButton);

        add(layoutPanel);
    }

    private void calculate()
    {
        var threeDosePrice = threeDoseTextField.getText();
        var fourDosePrice = fourDoseTextField.getText();

        if (threeDosePrice.isEmpty() || fourDosePrice.isEmpty())
        {
            updateCalculationResult("Please enter both prices.");
            return;
        }

        if (!threeDosePrice.matches("\\d+") || !fourDosePrice.matches("\\d+"))
        {
            updateCalculationResult("Please enter valid numeric values.");
            return;
        }

        var result = ProfitCalculatorHelper.calculateProfit3To4(
                Integer.parseInt(threeDosePrice),
                Integer.parseInt(fourDosePrice));

        updateCalculationResult(String.valueOf(result));
    }

    private void updateCalculationResult(String result)
    {
        calculationResultLabel.setText(result);
        repaint();
    }
}
