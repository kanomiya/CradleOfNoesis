package com.kanomiya.mcmod.cradleofnoesis.magic;

import net.minecraft.nbt.NBTTagCompound;

public class MagicNodeShapeSector implements IMagicNodeShape
{
	private int radius;
	private int angle;

	public MagicNodeShapeSector(int radius, int angle)
	{
		this.radius = radius;
		this.angle = angle;
	}

	public int getRadius()
	{
		return radius;
	}

	public int getAngle()
	{
		return angle;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setInteger("radius", radius);
		nbt.setInteger("angle", angle);

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		radius = nbt.getInteger("radius");
		angle = nbt.getInteger("angle");
	}

}
