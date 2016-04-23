package com.kanomiya.mcmod.cradleofnoesis.sanctuary;

import net.minecraft.nbt.NBTTagCompound;

import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;

/**
 * @author Kanomiya
 *
 */
public class SimpleSanctuary implements ISanctuary
{
	protected float radius;
	protected int color;

	public SimpleSanctuary(float radius, int color)
	{
		this.radius = radius;
		this.color = color;
	}


	/**
	 * @inheritDoc
	 */
	@Override
	public float getRadius()
	{
		return radius;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public int getColor()
	{
		return color;
	}


	/**
	 * @inheritDoc
	 */
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setFloat("radius", radius);
		nbt.setInteger("color", color);

		return nbt;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		radius = nbt.getFloat("radius");
		color = nbt.getInteger("color");

	}

}
