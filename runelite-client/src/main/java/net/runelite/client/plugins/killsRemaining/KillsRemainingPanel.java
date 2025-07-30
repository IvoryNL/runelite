package net.runelite.client.plugins.killsRemaining;

import net.runelite.api.Client;
import net.runelite.client.plugins.potionFlipper.Helpers.JElementHelper;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class KillsRemainingPanel extends PluginPanel
{
    private final float FONT_SIZE = 18f;

    private final Client client;

    private JLabel npcNameLabel;
    private JLabel npcMaxHpLabel;
    private JLabel remainingXpLabel;
    private JLabel remainingKillsLabel;

    @Inject
    public KillsRemainingPanel(Client client)
    {
        super();

        this.client = client;

        builtKillsRemainingPanel();
    }

    public void updateNpcName(String name)
    {
        npcNameLabel.setText("Name: " + name);
        repaint();
    }

    public void updateNpcMaxHp(int maxHp)
    {
        npcMaxHpLabel.setText("Max HP: " + maxHp);
        repaint();
    }

    public void updateRemainingXp(int remainingXp)
    {
        remainingXpLabel.setText("Remaining XP: " + remainingXp);
        repaint();
    }

    public void updateRemainingKills(int remainingKills) {
        remainingKillsLabel.setText("Remaining Kills: " + remainingKills);
        repaint();
    }

    private void builtKillsRemainingPanel()
    {
        var parent = getWrappedPanel();
        parent.setLayout(new BorderLayout());
        parent.setBackground(ColorScheme.DARK_GRAY_COLOR);
        parent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        var layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
        layoutPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        createLabels();

        layoutPanel.add(npcNameLabel);
        layoutPanel.add(npcMaxHpLabel);
        layoutPanel.add(remainingXpLabel);
        layoutPanel.add(remainingKillsLabel);

        parent.add(layoutPanel, BorderLayout.CENTER);
    }

    private void createLabels()
    {
        npcNameLabel = JElementHelper.createLabel("Name: ...", FONT_SIZE);
        npcMaxHpLabel = JElementHelper.createLabel("Max HP: ...", FONT_SIZE);
        remainingXpLabel = JElementHelper.createLabel("Remaining XP: ...", FONT_SIZE);
        remainingKillsLabel = JElementHelper.createLabel("Remaining Kills: ...", FONT_SIZE);
    }


}
