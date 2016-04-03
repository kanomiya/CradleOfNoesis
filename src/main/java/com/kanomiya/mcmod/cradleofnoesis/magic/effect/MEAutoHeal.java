package com.kanomiya.mcmod.cradleofnoesis.magic.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;

/**
 * @author Kanomiya
 *
 */
public class MEAutoHeal implements IMagicEffect, ITickable {

	public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(CradleOfNoesis.MODID, "autoHeal");

	protected MagicStatus magicStatus;
	protected int healTimer;

	public MEAutoHeal(MagicStatus magicStatus)
	{
		this.magicStatus = magicStatus;
		healTimer = 0;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void update()
	{
		healTimer ++;

		int interval = 80;
		if (magicStatus.hasOwner())
		{
			ICapabilityProvider owner = magicStatus.getOwner();
			if (owner instanceof EntityLivingBase)
			{
				EntityLivingBase entity = (EntityLivingBase) owner;
				if (entity.getMaxHealth() *0.8d <= entity.getHealth()) interval = 40;
			}
		}

		if (interval <= healTimer)
		{
			if (! magicStatus.isMpFull()) magicStatus.acceptMp(1);
			healTimer = 0;
		}
	}

	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setInteger("healTimer", healTimer);

		return nbt;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		healTimer = nbt.getInteger("healTimer");

	}


}
