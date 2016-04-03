package com.kanomiya.mcmod.cradleofnoesis.event;

import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Kanomiya
 *
 */
public class PlayerInteractionEventHandler {

	public static final PlayerInteractionEventHandler INSTANCE = new PlayerInteractionEventHandler();

	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		PlayerInteractEvent.Action action = event.action;

		switch (action)
		{
		case LEFT_CLICK_BLOCK:

			break;
		case RIGHT_CLICK_AIR:

			break;
		case RIGHT_CLICK_BLOCK:

			break;
		default:
			break;
		}

	}

	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event)
	{

	}


}
