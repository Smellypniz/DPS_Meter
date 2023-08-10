package com.dpsmeter;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;


@PluginDescriptor(
	name = "DPS Meter",
	description = "Displays player and team DPS",
	tags = {"dps", "damage", "meter"}
)
@Slf4j
public class DPSMeterPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private DPSMeterConfig config;
	@Inject
	@Getter
	private OverlayManager overlayManager;
	@Inject
	private MeterOverlay meterOverlay;
	private NPC recentlyDamagedNpc = null;
	private long startTime;
	private int totalDamage;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(meterOverlay);
		log.info("DPS Meter started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(meterOverlay);
		clearDPS();
		log.info("DPS Meter stopped!");

	}

	private void clearDPS() {
		startTime = 0;
		totalDamage = 0;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		//TODO
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event) {


		Player player = client.getLocalPlayer();
		Actor actor = event.getActor();

		if (actor instanceof NPC) {
			recentlyDamagedNpc = (NPC) actor;
		}

		// Get startTime if not currently running
		if (startTime == 0) {
			startTime = System.currentTimeMillis();
		}

		// If player's offensive hitsplat then add to totalDamage
		if (event.getHitsplat().isMine() && actor != player) {
			totalDamage += event.getHitsplat().getAmount();
			meterOverlay.setHitsplatApplied(true);
		}
	}

	public double calcDPS(int totalDamage, long startTime) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		double dps = totalDamage / (elapsedTime / 1000.0);
		return dps;
	}

	public double getCalculatedDPS() {
		return calcDPS(totalDamage, startTime);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned) {
		NPC npc = npcDespawned.getNpc();
		if (npc.isDead() && npc.equals(recentlyDamagedNpc)) {
			clearDPS();
			recentlyDamagedNpc = null;
		}
	}

	@Provides
	DPSMeterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DPSMeterConfig.class);
	}

}
