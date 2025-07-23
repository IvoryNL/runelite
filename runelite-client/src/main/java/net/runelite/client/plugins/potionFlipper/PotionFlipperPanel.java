package net.runelite.client.plugins.potionFlipper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.runelite.client.plugins.potionFlipper.Interfaces.IHttpService;
import net.runelite.client.plugins.potionFlipper.Interfaces.ISelectedPotionsService;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PotionFlipperPanel extends PluginPanel
{
    private static final String CARD_PRICING = "PRICING";
    private static final String CARD_SELECT   = "SELECT";

    private final IHttpService httpService;
    private final SelectPotionsPanel selectPotionsPanel;
    private final ISelectedPotionsService selectedPotionsService;
    private final JPanel cards;
    private final Gson gson;

    private JPanel layoutPanel;
    private ArrayList<ItemInfo> selectedItems;

    @Inject
    public PotionFlipperPanel(IHttpService httpService, SelectPotionsPanel selectPotionsPanel, ISelectedPotionsService selectedPotionsService, Gson gson) throws IOException
    {
        super();

        this.httpService = httpService;
        this.selectPotionsPanel = selectPotionsPanel;
        this.selectedPotionsService = selectedPotionsService;
        this.gson = gson;
        this.selectedItems = this.selectedPotionsService.getSelectedPotions();

        var parent = getWrappedPanel();
        parent.setLayout(new BorderLayout());

        cards = new JPanel(new CardLayout());
        cards.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        parent.add(cards, BorderLayout.CENTER);

        cards.add(buildPotionPricingPanel(), CARD_PRICING);
        cards.add(buildSelectPotionsPanel(), CARD_SELECT);

        ((CardLayout) cards.getLayout()).show(cards, CARD_PRICING);
    }

    public JPanel buildPotionPricingPanel() throws IOException {
        var pricingPanel = new JPanel(new BorderLayout());
        pricingPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        var buttonBar = new JPanel();
        buttonBar.setBackground(ColorScheme.DARK_GRAY_COLOR);

        var buttonPadding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        var buttonBorder = BorderFactory.createLineBorder(new Color(0, 0, 0), 2);
        var buttonCompound = BorderFactory.createCompoundBorder(buttonBorder, buttonPadding);

        var updatePricingPanel = new JButton("Update");
        updatePricingPanel.addActionListener(e -> updatePricingPanel());
        updatePricingPanel.setToolTipText("Update the potion pricing data from the API");
        updatePricingPanel.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        updatePricingPanel.setForeground(Color.WHITE);
        updatePricingPanel.setBorder(buttonCompound);

        var potionSelect = new JButton("Select");
        potionSelect.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, CARD_SELECT));
        potionSelect.setToolTipText("Select potions to flip");
        potionSelect.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        potionSelect.setForeground(Color.WHITE);
        potionSelect.setBorder(buttonCompound);

        buttonBar.add(updatePricingPanel, BorderLayout.WEST);
        buttonBar.add(potionSelect, BorderLayout.EAST);

        pricingPanel.add(buttonBar, BorderLayout.NORTH);

        this.layoutPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(this.layoutPanel, BoxLayout.Y_AXIS);
        this.layoutPanel.setLayout(boxLayout);

        addPotionPanels();

        pricingPanel.add(this.layoutPanel, BorderLayout.CENTER);

        return pricingPanel;
    }

    public JPanel buildSelectPotionsPanel()
    {
        selectPotionsPanel.setSelectionListener(selected ->
        {
            selectedPotionsService.setSelectedPotions(selected);
            selectedItems = new ArrayList<>(selected);

            updatePricingPanel();

            ((CardLayout) cards.getLayout()).show(cards, CARD_PRICING);
        });

        return selectPotionsPanel;
    }

    private void addPotionPanels() throws IOException
    {
        generatePricings();
    }

    private void updatePricingPanel()
    {
        updatePricingPanelAsync();
    }

    private void generatePricings() throws IOException {
        var mappedItems = CreateMapping();

        for (Map.Entry<String, ArrayList<ItemInfo>> mappedPotion : mappedItems.entrySet())
        {
            var potionMapperItems = mappedPotion.getValue();
            var threeDosePotionMapperItem = potionMapperItems.get(0);
            var fourDosePotionMapperItem = potionMapperItems.get(1);
            JPanel prayerPotionPanel = createPotionPanel(mappedPotion.getKey(), threeDosePotionMapperItem, fourDosePotionMapperItem);
            layoutPanel.add(prayerPotionPanel);
        }
    }
    private void updatePricingPanelAsync()
    {
        new SwingWorker<Void, Void>()
        {
            private Exception ex;
            @Override
            protected Void doInBackground()
            {
                try
                {
                    SwingUtilities.invokeLater(() -> layoutPanel.removeAll());
                    generatePricings();
                }
                catch (Exception e)
                {
                    ex = e;
                }
                return null;
            }

            @Override
            protected void done()
            {
                if (ex != null)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            PotionFlipperPanel.this,
                            "Failed to update potion pricing data: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                layoutPanel.revalidate();
                layoutPanel.repaint();
            }
        }.execute();
    }


    private JPanel createPotionPanel(String potionName, ItemInfo threeDose, ItemInfo fourDose) throws IOException
    {
        JPanel potionPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(potionPanel, BoxLayout.Y_AXIS);
        potionPanel.setLayout(boxLayout);
        potionPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        potionPanel.setForeground(Color.WHITE);
        potionPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        ItemPrice threeDoseItemPrice = getDataById(threeDose.id);
        JPanel threeDosePanel = getJPanelPotion(threeDose.name, threeDoseItemPrice);

        ItemPrice fourDoseItemPrice = getDataById(fourDose.id);
        JPanel fourDosePanel = getJPanelPotion(fourDose.name, fourDoseItemPrice);

        JPanel profit3to4Dose = getJPanelCalculated("Tree to Four Dose Profit",
                calculateProfit3To4(threeDoseItemPrice, fourDoseItemPrice));

        JPanel profit4to3Dose = getJPanelCalculated("Four to Three Dose Profit",
                calculateProfit4To3(fourDoseItemPrice, threeDoseItemPrice));

        potionPanel.add(threeDosePanel);
        potionPanel.add(fourDosePanel);
        potionPanel.add(profit3to4Dose);
        potionPanel.add(profit4to3Dose);

        return potionPanel;
    }

    private static JPanel getJPanelPotion(String potionName, ItemPrice ItemPrice)
    {
        JPanel potionPanel = new JPanel();
        JLabel potionLabel = new JLabel(potionName + ": ");
        potionLabel.setForeground(Color.WHITE);
        JLabel priceLabel = new JLabel(ItemPrice.low + "GP");
        priceLabel.setForeground(Color.WHITE);
        potionPanel.add(potionLabel);
        potionPanel.add(priceLabel);

        potionPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        potionPanel.setForeground(Color.WHITE);

        return potionPanel;
    }

    private static JPanel getJPanelCalculated(String labelText, int calculatedPrice)
    {
        JPanel profitPanel = new JPanel();
        JLabel profitLabel = new JLabel(labelText + ": " + calculatedPrice + "GP");
        profitLabel.setForeground(Color.WHITE);
        profitPanel.add(profitLabel);

        profitPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        profitPanel.setForeground(Color.WHITE);

        return profitPanel;
    }

    private ItemPrice getDataById(int id) throws IOException
    {
        String url = "https://prices.runescape.wiki/api/v1/osrs/latest?id=";
        String json = httpService.getById(id, url);

        return getFromJson(json);
    }

    private ItemPrice getFromJson(String json)
    {
        var response = gson.fromJson(json, ItemPriceResponse.class);

        return response.data.values().iterator().next();
    }

    private Map<String, ArrayList<ItemInfo>> CreateMapping()
    {
        Map<String, ArrayList<ItemInfo>> mappedItems = new HashMap<>();

        for (ItemInfo item : selectedItems)
        {
            var key = item.name.split("\\(")[0];
            if (!mappedItems.containsKey(key))
            {
                mappedItems.put(key, new ArrayList<>());
            }

            mappedItems.get(key).add(item);
        }

        return mappedItems;
    }

    private int calculateProfit3To4(ItemPrice threeDose, ItemPrice fourDose)
    {
        int priceFourDoseTaxDeducted = (int)(fourDose.low / 1.01);
        int priceThreeDoseTimesToFourDoses = threeDose.low / 3 * 4;

        return priceFourDoseTaxDeducted - priceThreeDoseTimesToFourDoses;
    }

    private int calculateProfit4To3(ItemPrice fourDose, ItemPrice threeDose)
    {
        int priceThreeDoseTaxDeducted = (int)(threeDose.low / 1.01);
        int priceFourDoseTimesToFourDoses = fourDose.low / 4 * 3;

        return priceThreeDoseTaxDeducted - priceFourDoseTimesToFourDoses;
    }
}
