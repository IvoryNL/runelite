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

        builtKillsRemainingPanel();
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

    private void builtKillsRemainingPanel()
    {
        var parent = getWrappedPanel();
        parent.setLayout(new BorderLayout());
        parent.setBackground(ColorScheme.DARK_GRAY_COLOR);

        var layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
        layoutPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        createLabels();

        layoutPanel.add(npcName);
        layoutPanel.add(npcMaxHp);
        layoutPanel.add(remainingXp);
        layoutPanel.add(remainingKills);

        parent.add(layoutPanel, BorderLayout.CENTER);
    }

    private void createLabels()
    {
        npcName = createLabel("Name: ...");
        npcMaxHp = createLabel("Max HP: ...");
        remainingXp = createLabel("Remaining XP: ...");
        remainingKills = createLabel("Remaining Kills: ...");
    }

    private JLabel createLabel(String text)
    {
        var label = new JLabel(text);
        label.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 18f));

        return label;
    }
}
