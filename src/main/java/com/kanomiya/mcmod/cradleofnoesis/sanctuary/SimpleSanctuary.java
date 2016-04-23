package com.kanomiya.mcmod.cradleofnoesis.sanctuary;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import com.google.common.collect.Lists;
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
	protected List<UUID> allowedUUIDList;

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

		if (allowedUUIDList != null)
		{
			NBTTagList nbtIdList = new NBTTagList();

			for (UUID uuid: allowedUUIDList)
			{
				NBTTagCompound nbtUuid = new NBTTagCompound();
				nbtUuid.setUniqueId("", uuid);

				nbtIdList.appendTag(nbtUuid);
			}

			nbt.setTag("allowedUUIDList", nbtIdList);
		}

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

		if (nbt.hasKey("allowedUUIDList", NBT.TAG_LIST))
		{
			if (allowedUUIDList == null) allowedUUIDList = Lists.newArrayList();
			else allowedUUIDList.clear();

			NBTTagList nbtIdList = nbt.getTagList("allowedUUIDList", NBT.TAG_COMPOUND);

			for (int i=0; i<nbtIdList.tagCount(); ++i)
			{
				NBTTagCompound nbtUuid = nbtIdList.getCompoundTagAt(i);
				allowedUUIDList.add(nbtUuid.getUniqueId(""));
			}
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void allowToEnter(Entity entity)
	{
		if (entity == null) return ;
		if (allowedUUIDList == null) allowedUUIDList = Lists.newArrayList();
		allowedUUIDList.add(entity.getUniqueID());
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void disallowToEnter(Entity entity)
	{
		if (entity != null && allowedUUIDList != null) allowedUUIDList.remove(entity.getUniqueID());
	}

	/**
	* @inheritDoc
	*/
	@Override
	public boolean isAllowedToEnter(Entity entity)
	{
		return entity != null && allowedUUIDList != null && allowedUUIDList.contains(entity.getUniqueID());
	}




}
