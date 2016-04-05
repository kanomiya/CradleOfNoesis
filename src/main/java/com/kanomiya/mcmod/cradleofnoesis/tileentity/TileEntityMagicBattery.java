package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.kanomiya.mcmod.cradleofnoesis.magic.ITickableWithMagicStatus;

/**
 * @author Kanomiya
 *
 */
public class TileEntityMagicBattery extends TileEntity implements ITickableWithMagicStatus.TileEntity {

	public TileEntityMagicBattery()
	{
		super();
	}

	@Override public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return new SPacketUpdateTileEntity(pos, 1, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}





}
