package com.kanomiya.mcmod.cradleofnoesis.magic.matter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author Kanomiya
 *
 */
public class MagicMatter implements INBTSerializable<NBTTagCompound> {

	public MagicMatter()
	{

	}

	public boolean canBeDropped()
	{
		return true;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("hello", "こんにちは");
		return nbt;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

	}

}
