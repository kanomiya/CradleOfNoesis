package com.kanomiya.mcmod.cradleofnoesis.util;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

import com.kanomiya.mcmod.cradleofnoesis.CONItems;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEmeraldTablet;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;

/**
 * @author Kanomiya
 *
 */
public class EnderEnergyUtils
{
	public static NBTTagCompound getEnderEnergyTag(ItemStack stack)
	{
		return stack.getSubCompound("enderEnergy", true);
	}

	public static void setEnderEnergyAmount(ItemStack stack, int amount)
	{
		getEnderEnergyTag(stack).setInteger("amount", amount);
	}

	public static void setEnderEnergyCapacity(ItemStack stack, int capacity)
	{
		getEnderEnergyTag(stack).setInteger("capacity", capacity);
	}

	public static int getEnderEnergyCapacity(ItemStack stack)
	{
		NBTTagCompound nbt = getEnderEnergyTag(stack);
		if (! nbt.hasKey("capacity", NBT.TAG_INT)) initEnderEnergy(stack);
		return nbt.getInteger("capacity");
	}

	public static int getEnderEnergyAmount(ItemStack stack)
	{
		NBTTagCompound nbt = getEnderEnergyTag(stack);
		if (! nbt.hasKey("amount", NBT.TAG_INT)) initEnderEnergy(stack);
		return nbt.getInteger("amount");
	}

	public static void initEnderEnergy(ItemStack stack)
	{
		if (stack.getItem() == Items.ender_pearl)
		{
			setEnderEnergyAmount(stack, 250);
			setEnderEnergyCapacity(stack, 250);
		}
		else if (stack.getItem() == CONItems.itemEmeraldTablet)
		{
			if (stack.getMetadata() == ItemEmeraldTablet.EnumType.LIA_FALIA.ordinal())
			{
				setEnderEnergyAmount(stack, 5000);
				setEnderEnergyCapacity(stack, 5000);
			} else if  (stack.getMetadata() == ItemEmeraldTablet.EnumType.LIA_STILIA.ordinal())
			{
				setEnderEnergyAmount(stack, 5000);
				setEnderEnergyCapacity(stack, 5000);
			} else if  (stack.getMetadata() == ItemEmeraldTablet.EnumType.LIA_REGILIA.ordinal())
			{
				setEnderEnergyAmount(stack, 5000);
				setEnderEnergyCapacity(stack, 5000);
			} else if  (stack.getMetadata() == ItemEmeraldTablet.EnumType.ARLEY.ordinal())
			{
				setEnderEnergyAmount(stack, 10000);
				setEnderEnergyCapacity(stack, 10000);
			} else if  (stack.getMetadata() == ItemEmeraldTablet.EnumType.ARMES.ordinal())
			{
				setEnderEnergyAmount(stack, 50000);
				setEnderEnergyCapacity(stack, 50000);
			}
		} else if (stack.getItem() == CONItems.itemIntelligentStone)
		{
			if (stack.getMetadata() == ItemIntelligentStone.EnumType.BLACK.ordinal())
			{
				setEnderEnergyAmount(stack, 5000);
				setEnderEnergyCapacity(stack, 5000);
			} else if  (stack.getMetadata() == ItemIntelligentStone.EnumType.WHITE.ordinal())
			{
				setEnderEnergyAmount(stack, 10000);
				setEnderEnergyCapacity(stack, 10000);
			} else if  (stack.getMetadata() == ItemIntelligentStone.EnumType.RED.ordinal())
			{
				setEnderEnergyAmount(stack, 50000);
				setEnderEnergyCapacity(stack, 50000);
			}
		}

	}


}
