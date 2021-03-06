package com.kanomiya.mcmod.cradleofnoesis.api.sanctuary;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;

/**
 *
 * @author Kanomiya
 *
 */
public class SimpleSanctuary implements ISanctuary
{
	protected String unlocalizedName;
	protected float radius;
	protected int age, maxAge;
	protected int color;
	protected List<UUID> allowedUUIDList;

	public SimpleSanctuary()
	{
		this(0f,0,0);
	}

	/**
	 *
	 * @param radius
	 * @param maxAge 32767(Short.MAX_VALUE)に設定すると寿命はなくなるよ
	 * @param color
	 */
	public SimpleSanctuary(float radius, int maxAge, int color)
	{
		this.radius = radius;
		this.maxAge = maxAge;
		this.color = color;
	}

	@Override
	public void onUpdate(World worldIn, double posX, double posY, double posZ)
	{
		if (getMaxAge() < Short.MAX_VALUE) ++age;
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
	public void setRadius(float radius)
	{
		this.radius = radius;
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
	public void getColor(int color)
	{
		this.color = color;
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
	public void setAge(int age)
	{
		this.age = age;
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
	public void setMaxAge(int maxAge)
	{
		this.maxAge = maxAge;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName == null ? "null" : unlocalizedName;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void setUnlocalizedName(String unlocalizedName)
	{
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, boolean advanced)
	{
		tooltip.add(I18n.format("vocabulary.sanctuary.type") + ": " + getLocalizedName());
		tooltip.add(I18n.format("vocabulary.sanctuary.radius") + ": " + getRadius());
		tooltip.add(I18n.format("vocabulary.sanctuary.color") + ": " + getColor());
		tooltip.add(I18n.format("vocabulary.sanctuary.maxAge") + ": " + getMaxAge());

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
