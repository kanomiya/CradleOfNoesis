package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;

/**
 * @author Kanomiya
 *
 */
public class TileEntityMagicBattery extends TileEntity implements ITickableWithMagicStatus {

	protected MagicStatus magicStatus;

	public TileEntityMagicBattery()
	{
		super();
		magicStatus = getCapability(CradleOfNoesisAPI.capMagicStatus, null);
	}

	@Override
	public MagicStatus getMagicStatus()
	{
		return magicStatus;
	}

	@Override public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(pos, 1, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}





}
