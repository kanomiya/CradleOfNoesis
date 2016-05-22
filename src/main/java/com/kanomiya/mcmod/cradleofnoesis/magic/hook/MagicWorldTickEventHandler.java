package com.kanomiya.mcmod.cradleofnoesis.magic.hook;

import com.kanomiya.mcmod.cradleofnoesis.magic.hook.world.WorldSavedDataMagic;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public enum MagicWorldTickEventHandler
{
	INSTANCE;

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		World worldIn = event.world;
		WorldSavedDataMagic worldMagicData = WorldSavedDataMagic.get(worldIn);

	}

}
