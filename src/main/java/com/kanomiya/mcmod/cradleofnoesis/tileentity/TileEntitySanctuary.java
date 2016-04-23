package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;

/**
 * @author Kanomiya
 *
 */
public class TileEntitySanctuary extends TileEntity
{
	private Entity sanctuaryEntity;

	public TileEntitySanctuary()
	{

	}

	/**
	 * @return sanctuaryEntity
	 */
	public Entity getSanctuaryEntity()
	{
		return sanctuaryEntity;
	}

	/**
	 * @param sanctuaryEntity セットする sanctuaryEntity
	 */
	public void setSanctuaryEntity(Entity sanctuaryEntity)
	{
		this.sanctuaryEntity = sanctuaryEntity;
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		if (nbt.hasUniqueId("sanctuaryEntity"))
		{
			UUID uuid = nbt.getUniqueId("sanctuaryEntity");
			List<Entity> list = worldObj.getEntities(EntitySanctuary.class, (i) -> i.getUniqueID().equals(uuid));
			if (! list.isEmpty()) sanctuaryEntity = list.get(0);

		}

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		if (sanctuaryEntity != null)
		{
			nbt.setUniqueId("sanctuaryEntity", sanctuaryEntity.getUniqueID());
		}

	}



}
