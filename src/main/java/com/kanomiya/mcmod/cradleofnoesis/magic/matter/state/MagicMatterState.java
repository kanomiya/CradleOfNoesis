package com.kanomiya.mcmod.cradleofnoesis.magic.matter.state;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author Kanomiya
 *
 */
public class MagicMatterState implements INBTSerializable<NBTTagCompound> {

	protected boolean updated;
	protected MagicMatterForm form;

	public MagicMatterState()
	{
		form = MagicMatterForm.BLOCK;
	}

	public MagicMatterForm getForm()
	{
		return form;
	}

	public void setForm(MagicMatterForm form)
	{
		this.form = form;
		updated = true;
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
		nbt.setString("form", form.name());
		return nbt;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		form = MagicMatterForm.valueOf(nbt.getString("form"));
		if (form == null) form = MagicMatterForm.BLOCK;

	}

}
