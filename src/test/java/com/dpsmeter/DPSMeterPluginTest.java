package com.dpsmeter;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DPSMeterPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DPSMeterPlugin.class);
		RuneLite.main(args);
	}
}