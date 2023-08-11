package com.dpsmeter;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Player;

import javax.inject.Inject;

public class DPSMeterCharacter {

    @Inject
    private final DPSMeterConfig config;
    @Inject
    private DPSMeterPlugin plugin;
    @Inject
    private Client client;
    @Getter
    private final Player player;
    @Getter
    private final Actor actor;
    @Getter
    @Setter
    private long startTime;
    @Getter
    @Setter
    private int totalDamage;
    @Getter
    @Setter
    private boolean hitsplatApplied;



    public DPSMeterCharacter(Player player, Actor actor, DPSMeterConfig config) {
        this.player = player;
        this.actor = actor;
        this.config = config;
    }

    double calculateValue() {

        switch (config.meterDisplayMode()) {
            case DPS:
                return calculateDPS();
            case DPT:
                return calculateDPS() * .6;
            case TOTAL_DAMAGE:
                return totalDamage;
            case DAMAGE_TAKEN:
                //TODO
                return 0.0; // Placeholder, replace with actual calculation
            default:
                return 0.0;
        }
    }

    public double calculateDPS() {
        if (startTime == 0) {
            return 0.0;
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        double dps = (double) totalDamage / (elapsedTime / 1000.0);

        return dps;
    }





}
