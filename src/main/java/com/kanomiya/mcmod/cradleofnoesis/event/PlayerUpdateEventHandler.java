package com.kanomiya.mcmod.cradleofnoesis.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;


/**
 * EntityPlayerのMagicStatusを更新する
 *
 * @author Kanomiya
 *
 */
public class PlayerUpdateEventHandler
{
	public static final PlayerUpdateEventHandler INSTANCE = new PlayerUpdateEventHandler();

	/**
	 *
	 * EntityPlayerのMagicStatusを更新する
	 *
	 * @param event LivingUpdateEvent
	 */
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		Entity entity = event.getEntity();

		if (! (entity instanceof EntityPlayer)) return ;
		if (! entity.hasCapability(CradleOfNoesisAPI.capMagicStatus, null)) return ;

		MagicStatus magicStatus = entity.getCapability(CradleOfNoesisAPI.capMagicStatus, null);

		magicStatus.update();
	}

}
