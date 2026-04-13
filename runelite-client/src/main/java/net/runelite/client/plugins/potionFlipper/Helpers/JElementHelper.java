package net.runelite.client.plugins.potionFlipper.Helpers;

import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import java.awt.*;

public final class JElementHelper
{
    public static JLabel createLabel(String text, float fontSize)
    {
        var label = new JLabel(text);
        label.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, fontSize));

        return label;
    }

    public static JButton createButton(String text, String toolTip)
    {
        var buttonPadding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        var buttonBorder = BorderFactory.createLineBorder(new Color(0, 0, 0), 2);
        var buttonCompound = BorderFactory.createCompoundBorder(buttonBorder, buttonPadding);

        var button = new JButton(text);
        button.setToolTipText(toolTip);
        button.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(buttonCompound);

        return button;
    }

    public static JTextField createTextField()
    {
        var textField = new JTextField(10);
        textField.setFont(textField.getFont().deriveFont(Font.PLAIN, 13f));

        return textField;
    }
}
