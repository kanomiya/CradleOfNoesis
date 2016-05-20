package com.kanomiya.mcmod.cradleofnoesis.sanctuary;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.SimpleSanctuary;

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
		super(radius, maxAge, 0xAA9ADE64);
		this.healAmount = healAmount;
		this.interval = interval;

		setUnlocalizedName("healSanctuary");
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

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, boolean advanced)
	{
		super.addInformation(tooltip, advanced);

		tooltip.add(I18n.format("vocabulary.sanctuary.interval") + ": " + getInterval());
		tooltip.add(I18n.format("vocabulary.sanctuary.healAmount") + ": " + getHealAmount());
	}

	public float getHealAmount()
	{
		return healAmount;
	}

	public void setHealAmount(float healAmount)
	{
		this.healAmount = healAmount;
	}

	/**
	 * @return interval
	 */
	public int getInterval()
	{
		return interval;
	}

	/**
	 * @param interval セットする interval
	 */
	public void setInterval(int interval)
	{
		this.interval = interval;
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
