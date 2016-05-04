package com.kanomiya.mcmod.cradleofnoesis.container.inventory;

import net.minecraft.inventory.IInventory;


public class FixedLimitedSlot extends FixedSlot {

	protected int stackLimit;

	public FixedLimitedSlot(IInventory par2IInventory, int slotNumber, int posX, int posY, int stackLimit) {
		super(par2IInventory, slotNumber, posX, posY);

		this.stackLimit = stackLimit;
	}

	@Override
	public int getSlotStackLimit()
	{
		return stackLimit;
	}

}
