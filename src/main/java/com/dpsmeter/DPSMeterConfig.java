package com.dpsmeter;

import com.dpsmeter.config.MeterDisplayMode;
import com.dpsmeter.config.MeterUpdateMode;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("dpsmeter")
public interface DPSMeterConfig extends Config {

	@ConfigSection(
			name = "Display",
			description = "All options that configure the meter display",
			position = 0
	)
	String meterDisplay = "displaySection";

	@ConfigSection(
			name = "Misc",
			description = "All other options",
			position = 1
	)
	String meterMisc = "miscSection";

	@ConfigItem(
		keyName = "meterDisplayMode",
		name = "Meter Display Mode",
		description = "The information type that is displayed on the meter",
		section = meterDisplay
	)
	default MeterDisplayMode meterDisplayMode() {
		return MeterDisplayMode.DPS;
	}

	@ConfigItem(
			keyName = "meterUpdateMode",
			name = "Meter Update Mode",
			description = "How and when the meter is updated",
			section = meterDisplay
	)
	default MeterUpdateMode meterUpdateMode() {
		return MeterUpdateMode.GAME_TICK;
	}

	@ConfigItem(
			keyName = "overlaySort",
			name = "Sort Damage",
			description = "Sorts damage list by player from highest damage to lowest",
			section = meterDisplay
	)
	default boolean overlaySort() {
		return true;
	}

	@ConfigItem(
			keyName = "resetDeath",
			name = "Reset on Death",
			description = "Resets meter on attacked NPC death",
			section = meterMisc
	)
	default boolean resetDeath() {
		return true;
	}


}
