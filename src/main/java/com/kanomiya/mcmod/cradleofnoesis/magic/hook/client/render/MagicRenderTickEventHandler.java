package com.kanomiya.mcmod.cradleofnoesis.magic.hook.client.render;

import com.kanomiya.mcmod.cradleofnoesis.magic.hook.world.WorldSavedDataMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public enum MagicRenderTickEventHandler
{
	INSTANCE;

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		World worldIn = mc.theWorld;
		EntityPlayer playerIn = mc.thePlayer;
		WorldSavedDataMagic worldMagicData = WorldSavedDataMagic.get(worldIn);





	}

}
