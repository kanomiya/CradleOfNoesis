package com.kanomiya.mcmod.cradleofnoesis.magic.matter.state;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author Kanomiya
 *
 */
public class MagicMatterState implements INBTSerializable<NBTTagCompound> {

	protected boolean updated;

	public MagicMatterState()
	{

	}

	public boolean isUpdated()
	{
		return updated;
	}

	public void removeUpdatedFlag()
	{
		updated = false;
	}


	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
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
