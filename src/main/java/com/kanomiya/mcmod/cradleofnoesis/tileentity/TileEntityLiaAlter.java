package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.Lists;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;

/**
 * @author Kanomiya
 *
 */
public class TileEntityLiaAlter extends ITileEntityWithInventory implements ITickableWithMagicStatus {

	protected MagicStatus magicStatus;

	private int furnaceBurnTime;
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;

	protected List<ItemStack> consumingStacks;

	public TileEntityLiaAlter()
	{
		consumingStacks = Lists.newArrayList();

		magicStatus = getCapability(CradleOfNoesisAPI.capMagicStatus, null);
	}

	@Override
	public MagicStatus getMagicStatus()
	{
		return magicStatus;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void update()
	{
		ITickableWithMagicStatus.super.update();

	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

	}


	public boolean isBurning()
	{
		return furnaceBurnTime > 0;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public int getSizeInventory() {
		return 3;
	}


	/**
	* @inheritDoc
	*/
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0) return stack.getItem() == Items.apple;
		if (1 <= index && index <= 2) return stack.getItem() == Items.ender_pearl;
		return false;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public String getName() {
		return "container.tileEntityLiaAlter";
	}


	@Override
	public int getField(int id)
	{
		switch (id)
		{
			case 0:
				return furnaceBurnTime;
			case 1:
				return currentItemBurnTime;
			case 2:
				return cookTime;
			case 3:
				return totalCookTime;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
			case 0:
				furnaceBurnTime = value;
				break;
			case 1:
				currentItemBurnTime = value;
				break;
			case 2:
				cookTime = value;
				break;
			case 3:
				totalCookTime = value;
		}
	}

	@Override
	public int getFieldCount()
	{
		return 4;
	}




}
