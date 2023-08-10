package com.dpsmeter;

import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class MeterOverlay extends OverlayPanel {

    private final DPSMeterPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();
    @Setter
    private boolean hitsplatApplied = false;
    private boolean overlayBuilt = false;
    @Inject
    private Client client;

    @Inject
    private MeterOverlay(DPSMeterPlugin plugin) {
        setPosition(OverlayPosition.TOP_LEFT); // Adjust position as needed
        this.plugin = plugin;
        setResizable(true);
    }


    // Method to update the overlay content
    private void updateOverlayContent(double value) {
        Player player = client.getLocalPlayer();
        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("DPS Meter")
                .color(Color.GREEN)
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left(player.getName())
                .right(String.format("%.2f", value))
                .build());
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!overlayBuilt) {
            // Initialize the overlay content
            updateOverlayContent(0.0);
            overlayBuilt = true;
            // Add the button overlay to the OverlayManager
        } else if (hitsplatApplied) {
            // Update overlay content with calculated DPS value
            double dps = plugin.getCalculatedDPS();
            updateOverlayContent(dps);
            hitsplatApplied = false;
        }

        // Render overlay content
        return panelComponent.render(graphics);
    }

}



