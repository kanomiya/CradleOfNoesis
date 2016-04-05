package com.kanomiya.mcmod.cradleofnoesis.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityMagicMatter;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemMagicMatter;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;
import com.kanomiya.mcmod.cradleofnoesis.magic.effect.MEAutoHeal;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.MagicMatter;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityMagicBattery;

/**
 * @author Kanomiya
 *
 */
public class AttachCapabilitiesEventHandler {

	public static final AttachCapabilitiesEventHandler INSTANCE = new AttachCapabilitiesEventHandler();

	@SubscribeEvent
	public void onAttachCapabilitiesEntityEvent(AttachCapabilitiesEvent.Entity event)
	{
		Entity target = event.getEntity();

		// ForgeEventFactory
		if (target instanceof EntityPlayer)
		{
			MagicStatus<EntityPlayer> magicStatus = MagicStatus.create(100, true, (EntityPlayer) target, null);
			magicStatus.addEffect(MEAutoHeal.RESOURCE_LOCATION, new MEAutoHeal(magicStatus));

			event.addCapability(CradleOfNoesisAPI.RL_MAGICSTATUS, magicStatus);
		}

		if (target instanceof EntityMagicMatter)
		{
			MagicStatus<EntityMagicMatter> magicStatus = MagicStatus.create(100, true, (EntityMagicMatter) target, new MagicMatter());

			event.addCapability(CradleOfNoesisAPI.RL_MAGICSTATUS, magicStatus);
		}

	}

	@SubscribeEvent
	public void onAttachCapabilitiesTileEntityEvent(AttachCapabilitiesEvent.TileEntity event)
	{
		TileEntity target = event.getTileEntity();

		// ForgeEventFactory
		if (target instanceof TileEntityMagicBattery)
		{
			MagicStatus<TileEntityMagicBattery> magicStatus = MagicStatus.create(100, false, (TileEntityMagicBattery) target, null);
			event.addCapability(CradleOfNoesisAPI.RL_MAGICSTATUS, magicStatus);
		}

	}

	@SubscribeEvent
	public void onAttachCapabilitiesItemEvent(AttachCapabilitiesEvent.Item event)
	{
		ItemStack target = event.getItemStack();
		Item targetItem = event.getItem();

		MagicStatus<ItemStack> magicStatus;

		// ForgeEventFactory
		if (targetItem instanceof ItemMagicMatter)
		{
			MagicMatter magicMatter = new MagicMatter();

			if (target.hasTagCompound())
			{
				NBTTagCompound nbt = target.getTagCompound();
				if (nbt.hasKey(CradleOfNoesis.MODID + ":defaultMagicMatter", NBT.TAG_COMPOUND))
				{
					NBTTagCompound nbtMatter = nbt.getCompoundTag(CradleOfNoesis.MODID + ":defaultMagicMatter");
					magicMatter.deserializeNBT(nbtMatter);
				}
			}

			magicStatus = MagicStatus.create(50, false, target, magicMatter);
			event.addCapability(CradleOfNoesisAPI.RL_MAGICSTATUS, magicStatus);
		}

	}

}
