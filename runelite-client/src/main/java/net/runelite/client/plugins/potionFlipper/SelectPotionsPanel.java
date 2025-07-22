package net.runelite.client.plugins.potionFlipper;

import com.google.gson.Gson;
import net.runelite.client.plugins.potionFlipper.Interfaces.IHttpService;
import net.runelite.client.plugins.potionFlipper.Interfaces.ISelectedPotionsService;
import net.runelite.client.plugins.potionFlipper.Interfaces.ISelectionListener;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;

public class SelectPotionsPanel extends PluginPanel
{
    private final Gson gson;
    private final IHttpService httpService;
    private final ISelectedPotionsService selectedPotionsService;
    private final JList<ItemInfo> allPotions;

    private ISelectionListener<ItemInfo> listener;

    @Inject
    public SelectPotionsPanel(Gson gson, IHttpService httpService, ISelectedPotionsService selectedPotionsService) throws IOException {
        super();

        this.gson = gson;
        this.httpService = httpService;
        this.selectedPotionsService = selectedPotionsService;

        allPotions = getItemInfoJList();

        var layoutPanel = buildSelectPotionPanel();
        add(layoutPanel, BorderLayout.CENTER);

        setJListSelectedPotions();
    }

    public void setSelectionListener(ISelectionListener<ItemInfo> listener)
    {
        this.listener = listener;
    }

    private JPanel buildSelectPotionPanel()
    {
        JPanel layoutPanel = new JPanel();
        layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));

        var done = getDoneButton();
        var scrollPane = getJScrollPane();

        layoutPanel.add(done, BorderLayout.NORTH);
        layoutPanel.add(scrollPane, BorderLayout.CENTER);

        return layoutPanel;
    }

    private JButton getDoneButton() {
        JButton done = new JButton("Done");
        done.setToolTipText("Finish selecting potions");
        done.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        done.setForeground(Color.WHITE);
        done.addActionListener(e -> {
            if (listener != null)
            {
                listener.selectionDone(new ArrayList<>(allPotions.getSelectedValuesList()));
            }
        });
        done.setAlignmentX(Component.CENTER_ALIGNMENT);

        return done;
    }

    private JList<ItemInfo> getItemInfoJList() throws IOException {
        final JList<ItemInfo> allPotions;
        ArrayList<ItemInfo> items = getData();
        allPotions = new JList<>(items.toArray(ItemInfo[]::new));
        allPotions.setCellRenderer((JList<? extends ItemInfo> list,
                                    ItemInfo value,
                                    int index,
                                    boolean isSelected,
                                    boolean cellHasFocus) -> {
            JLabel lbl = new JLabel(value.name + ": " + value.id);

            if (isSelected) {
                lbl.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
                lbl.setForeground(Color.BLACK);
            } else {
                lbl.setBackground(ColorScheme.DARK_GRAY_COLOR);
                lbl.setForeground(Color.WHITE);
            }

            lbl.setOpaque(true);
            return lbl;
        });

        allPotions.setSelectionModel(new DefaultListSelectionModel()
        {
            @Override
            public void setSelectionInterval(int index0, int index1)
            {
                if (isSelectedIndex(index0))
                {
                    super.removeSelectionInterval(index0, index1);
                }
                else
                {
                    super.addSelectionInterval(index0, index1);
                }
            }

            @Override
            public void addSelectionInterval(int index0, int index1)
            {
                setSelectionInterval(index0, index1);
            }
        });

        allPotions.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        allPotions.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        return allPotions;
    }

    private void setJListSelectedPotions()
    {
        ArrayList<ItemInfo> selectedPotions = selectedPotionsService.getSelectedPotions();
        if (selectedPotions != null && !selectedPotions.isEmpty())
        {
            for (ItemInfo potion : selectedPotions)
            {
                for (int i = 0; i < allPotions.getModel().getSize(); i++)
                {
                    if (allPotions.getModel().getElementAt(i).id == potion.id)
                    {
                        allPotions.addSelectionInterval(i, i);
                    }
                }
            }
        }
    }

    private JScrollPane getJScrollPane() {
        JScrollPane scrollPane = new JScrollPane(allPotions);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(null);
        scrollPane.setMinimumSize(new Dimension(0, 0));
        return scrollPane;
    }

    private ArrayList<ItemInfo> getData() throws IOException
    {
        String url = "https://www.osrsbox.com/osrsbox-db/items-summary.json";
        String json = httpService.getAll(url);

        return getFromJson(json);
    }

    private ArrayList<ItemInfo> getFromJson(String json)
    {
        Type itemListType = new TypeToken<Map<Integer, ItemInfo>>(){}.getType();
        Map<Integer, ItemInfo> response = gson.fromJson(json, itemListType);

        return response.values().stream()
                .filter(item -> item.name != null
                        && item.name.toLowerCase().contains("potion")
                        && (item.name.toLowerCase().contains("(3)") || item.name.toLowerCase().contains("(4)")))
                .sorted(Comparator.comparing(
                        item -> item.name,
                        String.CASE_INSENSITIVE_ORDER
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
