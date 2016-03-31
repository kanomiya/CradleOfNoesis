package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.Maps;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.energy.IHasHandyEnergy;
import com.kanomiya.mcmod.energyway.api.EnergyWayAPI;
import com.kanomiya.mcmod.energyway.api.energy.Energy;
import com.kanomiya.mcmod.energyway.api.energy.EnergyType;
import com.kanomiya.mcmod.energyway.api.energy.IHasEnergy;

/**
 * @author Kanomiya
 *
 */
public class TileEntityMagicBattery extends TileEntity implements IHasHandyEnergy {

	protected Map<EnergyType, Energy> energyMap;
	protected NBTTagCompound customData;

	public TileEntityMagicBattery()
	{
		energyMap = Maps.newHashMap();
		setEnergy(Energy.createEmpty(CradleOfNoesis.energyTypeMagic, 100));
	}

	/**
	* @inheritDoc
	*/
	@Override
	public Map<EnergyType, Energy> energyMap() {
		return energyMap;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public boolean canAccept(EnergyType energyType, IHasEnergy donor)
	{
		return energyType == CradleOfNoesis.energyTypeMagic;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public int getOnceChargeAmount(EnergyType energyType, IHasEnergy donor)
	{
		return 10;
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		deserializeEnergyOwnerNBT(nbt.getCompoundTag(EnergyWayAPI.ID_DATA));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setTag(EnergyWayAPI.ID_DATA, serializeEnergyOwnerNBT());
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

	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound getCustomData() {
		return customData;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void setCustomData(NBTTagCompound customData) {
		this.customData = customData;
	}




}
