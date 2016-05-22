package com.kanomiya.mcmod.cradleofnoesis.magic.hook.world;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WorldSavedDataMagic extends WorldSavedData
{
	public static final String DATA_NAME = CradleOfNoesisAPI.MODID + ":Magic";


	public WorldSavedDataMagic()
	{
		this(DATA_NAME);
	}

	public WorldSavedDataMagic(String name)
	{
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		return null;
	}


	public static WorldSavedDataMagic get(World worldIn)
	{
		WorldSavedDataMagic data = (WorldSavedDataMagic) worldIn.getPerWorldStorage().getOrLoadData(WorldSavedDataMagic.class, DATA_NAME);

		if (data == null)
		{
			data = new WorldSavedDataMagic();
			worldIn.getPerWorldStorage().setData(DATA_NAME, data);
		}

		return data;
	}


}
