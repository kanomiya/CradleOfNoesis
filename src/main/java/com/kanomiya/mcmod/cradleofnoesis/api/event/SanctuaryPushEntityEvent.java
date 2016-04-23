package com.kanomiya.mcmod.cradleofnoesis.api.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;

/**
 * @author Kanomiya
 *
 */
@Cancelable
public class SanctuaryPushEntityEvent extends Event
{
	protected ISanctuary sanctuary;
	protected Entity entity;

	public SanctuaryPushEntityEvent(ISanctuary sanctuary, Entity entity)
	{
		this.sanctuary = sanctuary;
		this.entity = entity;
	}

	/**
	 * @return sanctuary
	 */
	public ISanctuary getSanctuary()
	{
		return sanctuary;
	}

	/**
	 * @return entity
	 */
	public Entity getEntity()
	{
		return entity;
	}



}
