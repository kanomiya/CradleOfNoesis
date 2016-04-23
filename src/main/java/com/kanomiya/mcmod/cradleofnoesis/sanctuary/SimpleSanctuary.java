package com.kanomiya.mcmod.cradleofnoesis.sanctuary;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;

/**
 *
 * @author Kanomiya
 *
 */
public class SimpleSanctuary implements ISanctuary
{
	protected float radius;
	protected int age, maxAge;
	protected int color;

	public SimpleSanctuary()
	{
		this(0f,0,0);
	}

	public SimpleSanctuary(float radius, int maxAge, int color)
	{
		this.radius = radius;
		this.maxAge = maxAge;
		this.color = color;
	}

	@Override
	public void onUpdate(World worldIn, double posX, double posY, double posZ)
	{
		++age;
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
	public int getAge()
	{
		return age;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public int getMaxAge()
	{
		return maxAge;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setFloat("radius", radius);
		nbt.setInteger("age", age);
		nbt.setInteger("maxAge", maxAge);
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
		age = nbt.getInteger("age");
		maxAge = nbt.getInteger("maxAge");
		color = nbt.getInteger("color");

	}



}
