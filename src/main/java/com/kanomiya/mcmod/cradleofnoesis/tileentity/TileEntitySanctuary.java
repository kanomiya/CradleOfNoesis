package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;

/**
 * @author Kanomiya
 *
 */
public class TileEntitySanctuary extends TileEntity
{
	protected UUID sanctuaryUUID;

	public TileEntitySanctuary()
	{

	}

	/**
	 * @return sanctuaryEntity
	 */
	public EntitySanctuary getSanctuaryEntity()
	{
		EntitySanctuary sanctuaryEntity = null;

		List<EntitySanctuary> list = worldObj.getEntities(EntitySanctuary.class, (i) -> i != null && i.getSanctuaryUniqueID().equals(sanctuaryUUID));
		if (! list.isEmpty()) sanctuaryEntity = list.get(0);

		return sanctuaryEntity;
	}

	/**
	 * @param sanctuaryEntity セットする sanctuaryEntity
	 */
	public void setSanctuaryEntity(EntitySanctuary sanctuaryEntity)
	{
		sanctuaryUUID = sanctuaryEntity.getSanctuaryUniqueID();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		if (nbt.hasUniqueId("sanctuaryUUID"))
		{
			sanctuaryUUID = nbt.getUniqueId("sanctuaryUUID");
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);

		if (sanctuaryUUID != null)
		{
			nbt.setUniqueId("sanctuaryUUID", sanctuaryUUID);
		}

		return nbt;
	}



}
