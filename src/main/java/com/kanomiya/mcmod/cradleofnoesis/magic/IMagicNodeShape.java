package com.kanomiya.mcmod.cradleofnoesis.magic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMagicNodeShape extends INBTSerializable<NBTTagCompound>
{
	default int getCharacterX()
	{
		return 0;
	}

	default int getCharacterY()
	{
		return 0;
	}
}
