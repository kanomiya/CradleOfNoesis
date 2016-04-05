package com.kanomiya.mcmod.cradleofnoesis.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;


/**
 * @author Kanomiya
 *
 */
public class UpdateEventHandler {

	public static final UpdateEventHandler INSTANCE = new UpdateEventHandler();

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		Entity entity = event.getEntity();

		if (! entity.hasCapability(CradleOfNoesisAPI.capMagicStatus, null)) return ;

		MagicStatus magicStatus = entity.getCapability(CradleOfNoesisAPI.capMagicStatus, null);

		magicStatus.update();
	}

}
