package com.kanomiya.mcmod.cradleofnoesis.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class FixedSlot extends Slot {

	public FixedSlot(IInventory par2IInventory, int slotNumber, int posX, int posY) {
		super(par2IInventory, slotNumber, posX, posY);
	}

	@Override public boolean isItemValid(ItemStack stack) {
		return inventory.isItemValidForSlot(slotNumber, stack);
	}


}
