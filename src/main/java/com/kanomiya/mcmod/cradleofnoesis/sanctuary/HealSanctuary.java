package com.kanomiya.mcmod.cradleofnoesis.sanctuary;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author Kanomiya
 *
 */
public class HealSanctuary extends SimpleSanctuary
{
	protected int interval, timer;
	protected float healAmount;

	public HealSanctuary()
	{
		this(0f,0,0f,0);
	}

	public HealSanctuary(float radius, int maxAge, float healAmount, int interval)
	{
		super(radius, maxAge, 0x9ADE64AA);
		this.healAmount = healAmount;
		this.interval = interval;
	}


	@Override
	public void onUpdate(World worldIn, double posX, double posY, double posZ)
	{
		super.onUpdate(worldIn, posX, posY, posZ);

		++timer;
		if (interval < timer) timer = 0;
	}

	@Override
	public void onCollideWithEntity(World worldIn, double posX, double posY, double posZ, Entity entity)
	{
		super.onCollideWithEntity(worldIn, posX, posY, posZ, entity);

		if (interval < timer)
		{
			if (entity instanceof EntityLivingBase)
			{
				EntityLivingBase lvEntity = (EntityLivingBase) entity;

				lvEntity.heal(healAmount);
			}

			timer = 0;
		}

	}


	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();

		nbt.setInteger("interval", interval);
		nbt.setInteger("timer", timer);
		nbt.setFloat("healAmount", healAmount);

		return nbt;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);

		interval = nbt.getInteger("interval");
		timer = nbt.getInteger("timer");
		healAmount = nbt.getFloat("healAmount");
	}

}
