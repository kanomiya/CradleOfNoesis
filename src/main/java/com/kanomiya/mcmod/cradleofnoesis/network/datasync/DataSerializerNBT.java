package com.kanomiya.mcmod.cradleofnoesis.network.datasync;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

/**
 * @author Kanomiya
 *
 */
public class DataSerializerNBT implements DataSerializer<NBTTagCompound>
{
	/**
	* @inheritDoc
	*/
	@Override
	public void write(PacketBuffer buf, NBTTagCompound value)
	{
		buf.writeNBTTagCompoundToBuffer(value);
	}

	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound read(PacketBuffer buf) throws IOException
	{
		return buf.readNBTTagCompoundFromBuffer();
	}

	/**
	* @inheritDoc
	*/
	@Override
	public DataParameter<NBTTagCompound> createKey(int id)
	{
		return new DataParameter<NBTTagCompound>(id, this);
	}

}
