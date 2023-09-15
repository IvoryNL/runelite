package net.runelite.client.plugins.potionFlipper;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class PotionFlipperPanel extends PluginPanel
{
    private OsrsWikiApiHandler osrsWikiApiHandler = new OsrsWikiApiHandler();
    private Map<String, ArrayList<PotionMapperItem>> potionMapping = new HashMap<>();

    public PotionFlipperPanel()
    {
        super();

        var prayerPotionList = new ArrayList<PotionMapperItem>();
        prayerPotionList.add(new PotionMapperItem("Prayer potion (3)", 139));
        prayerPotionList.add(new PotionMapperItem("Prayer potion (4)", 2434));
        potionMapping.put("Prayer potion", prayerPotionList);

        var superRestoreList = new ArrayList<PotionMapperItem>();
        superRestoreList.add(new PotionMapperItem("Super restore (3)", 3026));
        superRestoreList.add(new PotionMapperItem("Super restore (4)", 3024));
        potionMapping.put("Super restore", superRestoreList);

        var staminaPotionList = new ArrayList<PotionMapperItem>();
        staminaPotionList.add(new PotionMapperItem("Stamina potion (3)", 12627));
        staminaPotionList.add(new PotionMapperItem("Stamina potion (4)", 12625));
        potionMapping.put("Stamina potion", staminaPotionList);

        var bastionPotionList = new ArrayList<PotionMapperItem>();
        bastionPotionList.add(new PotionMapperItem("Bastion potion (3)", 22464));
        bastionPotionList.add(new PotionMapperItem("Bastion potion (4)", 22461));
        potionMapping.put("Bastion potion", bastionPotionList);

        var saradominBrewList = new ArrayList<PotionMapperItem>();
        saradominBrewList.add(new PotionMapperItem("Saradomin brew (3)", 6687));
        saradominBrewList.add(new PotionMapperItem("Saradomin brew (4)", 6685));
        potionMapping.put("Saradomin brew", saradominBrewList);

        var superCombatList = new ArrayList<PotionMapperItem>();
        superCombatList.add(new PotionMapperItem("Super combat (3)", 12697));
        superCombatList.add(new PotionMapperItem("Super combat (4)", 12695));
        potionMapping.put("Super combat", superCombatList);

        var antiVenomList = new ArrayList<PotionMapperItem>();
        antiVenomList.add(new PotionMapperItem("Anti-venom (3)", 12907));
        antiVenomList.add(new PotionMapperItem("Anti-venom (4)", 12905));
        potionMapping.put("Anti-venom", antiVenomList);

        var antiVenomPlusList = new ArrayList<PotionMapperItem>();
        antiVenomPlusList.add(new PotionMapperItem("Anti-venom+ (3)", 12915));
        antiVenomPlusList.add(new PotionMapperItem("Anti-venom+ (4)", 12913));
        potionMapping.put("Anti-venom+", antiVenomPlusList);

        setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JPanel layoutPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(layoutPanel, BoxLayout.Y_AXIS);
        layoutPanel.setLayout(boxLayout);
        add(layoutPanel, BorderLayout.NORTH);

        JButton updateButton = new JButton();
        updateButton.setText("Update");
        updateButton.addActionListener(e -> updatePotionPanels(layoutPanel));

        layoutPanel.add(updateButton);
        addPotionPanels(layoutPanel);
    }

    private void addPotionPanels(JPanel layoutPanel)
    {
        for (Map.Entry<String, ArrayList<PotionMapperItem>> mappedPotion : potionMapping.entrySet())
        {
            var potionMapperItems = mappedPotion.getValue();
            var threeDosePotionMapperItem = potionMapperItems.get(0);
            var fourDosePotionMapperItem = potionMapperItems.get(1);
            JPanel prayerPotionPanel = createPotionPanel(mappedPotion.getKey(), threeDosePotionMapperItem, fourDosePotionMapperItem);
            layoutPanel.add(prayerPotionPanel);
        }
    }

    private void updatePotionPanels(JPanel layoutPanel)
    {
        for (Component component : layoutPanel.getComponents())
        {
            if (component instanceof JPanel)
            {
                layoutPanel.remove(component);
            }
        }

        addPotionPanels(layoutPanel);
        layoutPanel.validate();
        layoutPanel.repaint();
    }

    private JPanel createPotionPanel(String potionName, PotionMapperItem threeDosePotion, PotionMapperItem fourDosePotion)
    {
        JPanel potionPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(potionPanel, BoxLayout.Y_AXIS);
        potionPanel.setLayout(boxLayout);

        OsrsWikiItemData osrsWikiItemDataThreeDoses = getOsrsWikiItemData(threeDosePotion.id);
        JPanel threeDosePanel = new JPanel();
        threeDosePanel.add(new JLabel(threeDosePotion.name + ": "));
        threeDosePanel.add(new JLabel(osrsWikiItemDataThreeDoses.high + "GP"));

        OsrsWikiItemData osrsWikiItemDataFourDoses = getOsrsWikiItemData(fourDosePotion.id);
        JPanel fourDosePanel = new JPanel();
        fourDosePanel.add(new JLabel(fourDosePotion.name + ": "));
        fourDosePanel.add(new JLabel(osrsWikiItemDataFourDoses.high + "GP"));

        JPanel resultPanel = new JPanel();
        resultPanel.add(new JLabel(potionName + ": "));
        int potionDecantProfitValue = calculateProfit(osrsWikiItemDataThreeDoses, osrsWikiItemDataFourDoses);
        resultPanel.add(new JLabel(potionDecantProfitValue + "GP"));

        potionPanel.add(threeDosePanel);
        potionPanel.add(fourDosePanel);
        potionPanel.add(resultPanel);
        potionPanel.add(new JLabel("-------------------"));

        return potionPanel;
    }

    private OsrsWikiItemData getOsrsWikiItemData(int id)
    {
        try
        {
            return osrsWikiApiHandler.GetOsrsWikiResponse(id);
        }
        catch (IOException exception)
        {
            return new OsrsWikiItemData();
        }
    }

    private int calculateProfit(OsrsWikiItemData osrsWikiItemDataThreeDoses, OsrsWikiItemData osrsWikiItemDataFourDoses)
    {
        int priceFourDoseTaxDeducted = (int)(osrsWikiItemDataFourDoses.high / 1.01);
        int priceThreeDoseTimesToFourDoses = osrsWikiItemDataThreeDoses.high / 3 * 4;
        int result = priceFourDoseTaxDeducted - priceThreeDoseTimesToFourDoses;
        return result;
    }
}
