package net.runelite.client.plugins.potionFlipper;

import com.google.gson.Gson;
import net.runelite.client.plugins.potionFlipper.Constants.Urls;
import net.runelite.client.plugins.potionFlipper.Helpers.JElementHelper;
import net.runelite.client.plugins.potionFlipper.Helpers.ProfitCalculatorHelper;
import net.runelite.client.plugins.potionFlipper.Interfaces.IHttpService;
import net.runelite.client.plugins.potionFlipper.Interfaces.ISelectedPotionsService;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PotionFlipperPanel extends PluginPanel
{
    private static final String CARD_PRICING = "PRICING";
    private static final String CARD_SELECT = "SELECT";
    private static final String CARD_MANUAL = "MANUAL";

    private final IHttpService httpService;
    private final SelectPotionsPanel selectPotionsPanel;
    private final ManualCalculationPanel manualCalculationPanel;
    private final ISelectedPotionsService selectedPotionsService;
    private final Gson gson;

    private JPanel cards;
    private JPanel layoutPanel;
    private ArrayList<ItemInfo> selectedItems;

    @Inject
    public PotionFlipperPanel(IHttpService httpService, SelectPotionsPanel selectPotionsPanel,
                              ISelectedPotionsService selectedPotionsService, Gson gson,
                              ManualCalculationPanel manualCalculationPanel) throws IOException
    {
        super();

        this.httpService = httpService;
        this.selectPotionsPanel = selectPotionsPanel;
        this.manualCalculationPanel = manualCalculationPanel;
        this.selectedPotionsService = selectedPotionsService;
        this.gson = gson;
        this.selectedItems = this.selectedPotionsService.getSelectedPotions();

        buildPanels();
    }

    private void buildPanels() throws IOException
    {
        var parent = getWrappedPanel();
        parent.setLayout(new BorderLayout());
        parent.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        buildCards();

        parent.add(cards, BorderLayout.CENTER);
    }

    private void buildCards() throws IOException
    {
        cards = new JPanel(new CardLayout());
        cards.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        cards.add(buildPotionPricingPanel(), CARD_PRICING);
        cards.add(buildSelectPotionsPanel(), CARD_SELECT);
        cards.add(buildManualCalculationPanel(), CARD_MANUAL);

        ((CardLayout) cards.getLayout()).show(cards, CARD_PRICING);
    }

    private JPanel buildPotionPricingPanel() throws IOException
    {
        var pricingPanel = new JPanel(new BorderLayout());
        pricingPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        var updatePricingPanel = JElementHelper.createButton("Update", "Update the potion pricing data from the API");
        updatePricingPanel.addActionListener(e -> updatePricingPanel());

        var potionSelect = JElementHelper.createButton("Select", "Select potions to flip");
        potionSelect.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, CARD_SELECT));

        var manualCalc = JElementHelper.createButton("Manual", "Go to manual calculation panel");
        manualCalc.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, CARD_MANUAL));

        var buttonBar = createButtonBar();
        buttonBar.add(updatePricingPanel, BorderLayout.WEST);
        buttonBar.add(potionSelect, BorderLayout.CENTER);
        buttonBar.add(manualCalc, BorderLayout.EAST);


        this.layoutPanel = new JPanel();
        this.layoutPanel.setLayout(new BoxLayout(this.layoutPanel, BoxLayout.Y_AXIS));
        this.layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        var scroll = new JScrollPane(layoutPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        addPotionPanels();

        pricingPanel.add(buttonBar, BorderLayout.NORTH);
        pricingPanel.add(scroll, BorderLayout.CENTER);

        return pricingPanel;
    }

    private JPanel createButtonBar()
    {
        var buttonBar = new JPanel();
        buttonBar.setBackground(ColorScheme.DARK_GRAY_COLOR);

        return buttonBar;
    }



    private JPanel buildSelectPotionsPanel()
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

    private JPanel buildManualCalculationPanel()
    {
        var backButton = JElementHelper.createButton("Back", "Go back to the potion pricing panel");
        backButton.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, CARD_PRICING));

        manualCalculationPanel.add(backButton, BorderLayout.NORTH);

        return manualCalculationPanel;
    }

    private void addPotionPanels() throws IOException
    {
        generatePricings();
    }

    private void updatePricingPanel()
    {
        updatePricingPanelAsync();
    }

    private JPanel createPotionPanel(ItemInfo threeDose, ItemInfo fourDose) throws IOException
    {
        var layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
        layoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        layoutPanel.setForeground(Color.WHITE);
        layoutPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        var threeDoseItemPrice = getDataById(threeDose.id);
        var fourDoseItemPrice = getDataById(fourDose.id);
        var threeDosePanel = getJPanelPotion(threeDose.name, threeDoseItemPrice);
        var fourDosePanel = getJPanelPotion(fourDose.name, fourDoseItemPrice);

        var profit3to4Dose = getJPanelCalculated("Tree to Four Dose Profit",
                ProfitCalculatorHelper.calculateProfit3To4(threeDoseItemPrice.low, fourDoseItemPrice.low));

        layoutPanel.add(threeDosePanel);
        layoutPanel.add(fourDosePanel);
        layoutPanel.add(profit3to4Dose);

        Dimension pref = layoutPanel.getPreferredSize();
        layoutPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        layoutPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return layoutPanel;
    }

    private JPanel getJPanelPotion(String potionName, ItemPrice ItemPrice)
    {
        var potionPanel = new JPanel();
        potionPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        potionPanel.setForeground(Color.WHITE);

        var potionLabel = createLabel(potionName + ": ");
        var priceLabel = createLabel(ItemPrice.low + "GP");

        potionPanel.add(potionLabel);
        potionPanel.add(priceLabel);

        return potionPanel;
    }

    private JPanel getJPanelCalculated(String labelText, int calculatedPrice)
    {
        var profitPanel = new JPanel();
        profitPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        profitPanel.setForeground(Color.WHITE);

        var profitLabel = createLabel(labelText + ": " + calculatedPrice + "GP");

        profitPanel.add(profitLabel);

        return profitPanel;
    }

    private JLabel createLabel(String text)
    {
        var label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 13));

        return label;
    }

    private ItemPrice getDataById(int id) throws IOException
    {
        final var url = Urls.ItemPriceUrl + "?id=";
        final var json = httpService.getById(id, url);

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

        for (var item : selectedItems)
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

    private void generatePricings() throws IOException
    {
        var mappedItems = CreateMapping();

        for (var mappedPotion : mappedItems.entrySet())
        {
            var potionMapperItems = mappedPotion.getValue();
            var threeDosePotionMapperItem = potionMapperItems.get(0);
            var fourDosePotionMapperItem = potionMapperItems.get(1);
            var prayerPotionPanel = createPotionPanel(threeDosePotionMapperItem, fourDosePotionMapperItem);

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

                    layoutPanel.revalidate();
                    layoutPanel.repaint();
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
            }
        }.execute();
    }
}
