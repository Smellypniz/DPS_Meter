package com.dpsmeter;

import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

public class DPSMeterOverlay extends OverlayPanel {

    private final DPSMeterPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();
    private final DPSMeterConfig config;
    @Setter
    private boolean hitsplatApplied = false;
    private boolean overlayBuilt = false;
    @Inject
    private Client client;
    private boolean overlaySort;
    private List<DPSMeterCharacter> characters;


    @Inject
    private DPSMeterOverlay(DPSMeterPlugin plugin, DPSMeterConfig config) {
        setPosition(OverlayPosition.TOP_LEFT); // Adjust position as needed
        this.plugin = plugin;
        this.config = config;
        setResizable(true);
        overlaySort = plugin.getConfig().overlaySort(); // boolean for sorting by dps
        this.characters = plugin.getCharacters();
    }

    private int comparePlayers() {
        if (overlaySort) {
            // Implement sorting logic based on selected display mode
            // Use sorting mode to decide how to sort
        }
        return 0; // Return 0 if no sorting is applied
    }


    // Method to update the overlay content
    public void updateOverlayContent(List<DPSMeterCharacter> characters) {
        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("DPS Meter")
                .color(Color.GREEN)
                .build());

        for (DPSMeterCharacter character : characters) {
            double value = character.calculateValue();
            String displayName = character.getPlayer().getName();

            System.out.println("Character: " + displayName + " - Value: " + value); // Debug log


            panelComponent.getChildren().add(LineComponent.builder()
                    .left(displayName)
                    .right(String.format("%.2f", value))
                    .build());
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!overlayBuilt) {
            // Initialize the overlay content
            updateOverlayContent(characters);
            overlayBuilt = true;
        } else if (hitsplatApplied) {
            // Update overlay content with given value
            updateOverlayContent(characters);
            hitsplatApplied = false;
        }

        // Render overlay content
        return panelComponent.render(graphics);
    }

}



