package com.kanomiya.mcmod.cradleofnoesis.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;
import com.kanomiya.mcmod.cradleofnoesis.magic.effect.MEAutoHeal;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityMagicBattery;

/**
 * @author Kanomiya
 *
 */
public class AttachCapabilitiesEventHandler {

	public static final AttachCapabilitiesEventHandler INSTANCE = new AttachCapabilitiesEventHandler();

	@SubscribeEvent
	public void onAttachCapabilitiesEntityEvent(AttachCapabilitiesEvent.Entity event)
	{
		Entity target = event.getEntity();

		// ForgeEventFactory
		if (target instanceof EntityPlayer)
		{
			MagicStatus<EntityPlayer> magicStatus = MagicStatus.create(100, true, (EntityPlayer) target);
			magicStatus.addEffect(MEAutoHeal.RESOURCE_LOCATION, new MEAutoHeal(magicStatus));

			event.addCapability(CradleOfNoesisAPI.RL_MAGICSTATUS, magicStatus);
		}

	}

	@SubscribeEvent
	public void onAttachCapabilitiesEntityEvent(AttachCapabilitiesEvent.TileEntity event)
	{
		TileEntity target = event.getTileEntity();

		// ForgeEventFactory
		if (target instanceof TileEntityMagicBattery)
		{
			MagicStatus<TileEntityMagicBattery> magicStatus = MagicStatus.create(100, false, (TileEntityMagicBattery) target);
			event.addCapability(CradleOfNoesisAPI.RL_MAGICSTATUS, magicStatus);
		}

	}

}
