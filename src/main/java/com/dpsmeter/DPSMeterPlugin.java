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
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
import java.util.List;


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
	@Getter
	private DPSMeterConfig config;
	@Inject
	@Getter
	private OverlayManager overlayManager;
	@Inject
	private DPSMeterOverlay dpsMeterOverlay;
	private NPC recentlyDamagedNpc = null;
	private long startTime;
	private int totalDamage;
	@Getter
	private List<DPSMeterCharacter> characters = new ArrayList<>();

	@Provides
	DPSMeterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DPSMeterConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(dpsMeterOverlay);
		log.info("DPS Meter started!");
		System.out.println(config.meterDisplayMode());
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(dpsMeterOverlay);
		clearDPS();
		log.info("DPS Meter stopped!");

	}


	private void clearDPS() {
		startTime = 0;
		totalDamage = 0;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		//TODO
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event) {
		Player player = client.getLocalPlayer();
		Actor actor = event.getActor();

		if (actor instanceof NPC) {
			DPSMeterCharacter character = findCharacterByPlayer(player);
			if (character == null) {
				character = new DPSMeterCharacter(player, actor, config);
				characters.add(character);
			}

			// Get startTime if not currently running
			if (character.getStartTime() == 0) {
				character.setStartTime(System.currentTimeMillis());
			}

			// If player's offensive hitsplat then add to totalDamage and set overlay hitsplatApplied flag true
			if (event.getHitsplat().isMine() && actor != player) {
				int damageAmount = event.getHitsplat().getAmount();
				character.setTotalDamage(character.getTotalDamage() + damageAmount);
				character.setHitsplatApplied(true);
				dpsMeterOverlay.setHitsplatApplied(true);
				System.out.println(characters);
			}
		}
	}

	private DPSMeterCharacter findCharacterByPlayer(Player player) {
		for (DPSMeterCharacter character : characters) {
			if (character.getPlayer().equals(player)) {
				return character;
			}
		}
		return null;
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned) {
		NPC npc = npcDespawned.getNpc();
		if (npc.isDead() && npc.equals(recentlyDamagedNpc)) {
			clearDPS();
			recentlyDamagedNpc = null;
		}
	}
	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged) {
		if (configChanged.getGroup().equals("dpsmeter")) {
			dpsMeterOverlay.updateOverlayContent(characters);
		}
	}





}
