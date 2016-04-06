package com.kanomiya.mcmod.cradleofnoesis.magic.matter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import com.kanomiya.mcmod.cradleofnoesis.CONMagicMatterTypes;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.state.MagicMatterState;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.type.IMagicMatterType;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.type.MagicMatterTypeRegistry;

/**
 * @author Kanomiya
 *
 */
public class MagicMatter implements INBTSerializable<NBTTagCompound> {

	protected IMagicMatterType type;
	protected MagicMatterState state;
	protected boolean updated;

	public MagicMatter()
	{
		type = CONMagicMatterTypes.UNKNOWN;
		state = new MagicMatterState();
		updated = true;
	}

	public MagicMatter(IMagicMatterType type)
	{
		this();
		this.type = type;
		updated = true;
	}

	public IMagicMatterType getMatterType()
	{
		return type;
	}

	public MagicMatterState getMatterState()
	{
		return state;
	}

	public void setMatterType(IMagicMatterType type)
	{
		this.type = type;
		updated = true;
	}


	public String getUnlocalizedName()
	{
		return type.getUnlocalizedName(state);
	}

	public String getDisplayName()
	{
		return type.getDisplayName(state);
	}

	public float getDropChance()
	{
		return type.getDropChance(state);
	}

	public boolean isUpdated()
	{
		return updated || state.isUpdated();
	}

	public void removeUpdatedFlag()
	{
		updated = false;
		state.removeUpdatedFlag();
	}

	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("typeId", MagicMatterTypeRegistry.INSTANCE.getIdFromType(type).toString());
		nbt.setTag("state", state.serializeNBT());
		return nbt;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		type = MagicMatterTypeRegistry.INSTANCE.getTypeFromId(new ResourceLocation(nbt.getString("typeId")));
		state.deserializeNBT(nbt.getCompoundTag("state"));
	}

}
