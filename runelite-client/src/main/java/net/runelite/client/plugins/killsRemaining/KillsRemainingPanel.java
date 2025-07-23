package net.runelite.client.plugins.killsRemaining;

import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class KillsRemainingPanel extends PluginPanel
{
    private final Client client;

    private JLabel npcName;
    private JLabel npcMaxHp;
    private JLabel remainingXp;
    private JLabel remainingKills;

    @Inject
    public KillsRemainingPanel(Client client)
    {
        super();

        this.client = client;

        var parent = getWrappedPanel();
        parent.setLayout(new BorderLayout());
        parent.setBackground(ColorScheme.DARK_GRAY_COLOR);

        var layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
        layoutPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        npcName = new JLabel("Name: ...");
        npcName.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        npcName.setForeground(Color.WHITE);

        npcMaxHp = new JLabel("Max HP: ...");
        npcMaxHp.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        npcMaxHp.setForeground(Color.WHITE);

        remainingXp = new JLabel("Remaining XP: ...");
        remainingXp.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        remainingXp.setForeground(Color.WHITE);

        remainingKills = new JLabel("Remaining Kills: ...");
        remainingKills.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        remainingKills.setForeground(Color.WHITE);

        layoutPanel.add(npcName);
        layoutPanel.add(npcMaxHp);
        layoutPanel.add(remainingXp);
        layoutPanel.add(remainingKills);

        parent.add(layoutPanel, BorderLayout.CENTER);
    }

    public void updateNpcName(String name)
    {
        npcName.setText("Name: " + name);
        repaint();
    }

    public void updateNpcMaxHp(int maxHp)
    {
        npcMaxHp.setText("Max HP: " + maxHp);
        repaint();
    }

    public void updateRemainingXp(int remainingXp)
    {
        this.remainingXp.setText("Remaining XP: " + remainingXp);
        repaint();
    }

    public void updateRemainingKills(int remainingKills) {
        this.remainingKills.setText("Remaining Kills: " + remainingKills);
        repaint();
    }
}
