package net.runelite.client.plugins.killsRemaining;

import com.google.common.base.Strings;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.NPCManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.image.BufferedImage;

@PluginDescriptor(
        name = "KillsRemaining",
        description = "Enable the Kills Remaining panel",
        tags = {"panel"}
)
public class KillsRemainingPlugin extends Plugin
{
    @Inject
    private Client client;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private KillsRemainingPanel killsRemainingPanel;

    @Inject
    private NPCManager npcManager;

    private NavigationButton navKillsRemaining;

    private Actor lastOpponent;

    @Override
    protected void startUp() throws Exception
    {
        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "combat.png");

        navKillsRemaining = NavigationButton.builder()
                .tooltip("Kills Remaining")
                .icon(icon)
                .priority(12)
                .panel(killsRemainingPanel)
                .build();

        clientToolbar.addNavigation(navKillsRemaining);
    }

    @Override
    protected void shutDown() throws Exception
    {
        clientToolbar.removeNavigation(navKillsRemaining);
    }

    @Subscribe
//    public void onGameTick(GameTick tick)
    public void onInteractingChanged(InteractingChanged ev)
    {
        Actor opponent = client.getLocalPlayer().getInteracting();

        if (opponent instanceof NPC) {
            if (opponent == lastOpponent) {
                return; // No change in opponent
            }
            lastOpponent = opponent;

            int maxHp = npcManager.getHealth(((NPC) opponent).getId());
            killsRemainingPanel.updateNpcName(opponent.getName());
            killsRemainingPanel.updateNpcMaxHp(maxHp);
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged ev)
    {
        Skill skill = ev.getSkill();
        if (isCombatSkill(skill))
        {
            updateCombatSkillInfo(skill);
        }
    }

    private boolean isCombatSkill(Skill s)
    {
        return s == Skill.ATTACK
                || s == Skill.STRENGTH
                || s == Skill.DEFENCE
                || s == Skill.RANGED;
    }

    private void updateCombatSkillInfo(Skill skill)
    {
        Actor opponent = client.getLocalPlayer().getInteracting();

        if (opponent instanceof NPC) {
            String skillName = skill.getName();
            int currentXp = client.getSkillExperience(skill);
            int currentLevel = client.getRealSkillLevel(skill);
            int xpForNext = Experience.getXpForLevel(currentLevel + 1);
            int remainingXp = xpForNext - currentXp;
            int maxHp = npcManager.getHealth(((NPC) opponent).getId());

            killsRemainingPanel.updateRemainingXp(remainingXp);
            killsRemainingPanel.updateRemainingKills(remainingXp / (maxHp * 4));
        }
    }
}
