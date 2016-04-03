package com.kanomiya.mcmod.cradleofnoesis.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;

public class ContainerTileEntityLiaAlter extends Container {

	protected TileEntityLiaAlter tileEntity;
	int slotNum = 0;

	public ContainerTileEntityLiaAlter(InventoryPlayer inventoryPlayer, TileEntityLiaAlter te) {
		tileEntity = te;

		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen

		addSlotToContainer(new FixedSlot(tileEntity, slotNum, 80, 25));
		++slotNum;

		addSlotToContainer(new FixedLimitedSlot(tileEntity, slotNum, 62, 65, 1));
		++slotNum;
		addSlotToContainer(new FixedLimitedSlot(tileEntity, slotNum, 98, 65, 1));
		++slotNum;


		//commonly used vanilla code that adds the player's inventory
		bindPlayerInventory(inventoryPlayer);

		tileEntity.openInventory(inventoryPlayer.player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}


	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 101 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 159));
		}
	}

	/**
	 * シフトクリック時
	 */
	@Override public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotNum) {
		Slot slot = inventorySlots.get(slotNum);

		if (slot == null || ! slot.getHasStack()) return null;

		ItemStack slotStack = slot.getStack();

		if (slotNum < tileEntity.getSizeInventory())
		{
			if (! mergeItemStack(slotStack, tileEntity.getSizeInventory(), inventorySlots.size(), true))
			{
				if (slotStack.stackSize == 0) slot.putStack((ItemStack) null);
				return null;
			}
		}
		else
		{
			if (! mergeItemStack(slotStack, 0, tileEntity.getSizeInventory(), false))
			{
				if (slotStack.stackSize == 0) slot.putStack((ItemStack) null);
				return null;
			}
		}


		if (slotStack.stackSize == 0)
		{
			slot.putStack((ItemStack) null);
			return null;
		}

		return slotStack;
	}




	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);
		tileEntity.closeInventory(p_75134_1_);
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean flag = false;
		int i = startIndex;

		if (reverseDirection)
		{
			i = endIndex - 1;
		}

		if (stack.isStackable())
		{
			while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex))
			{
				Slot slot = inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();

				if (itemstack != null && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack))
				{
					int j = itemstack.stackSize + stack.stackSize;
					int k = Math.min(slot.getItemStackLimit(itemstack), itemstack.getMaxStackSize());

					if (j <= k)
					{
						stack.stackSize = 0;
						itemstack.stackSize = j;
						slot.onSlotChanged();
						flag = true;
					}
					else if (itemstack.stackSize < k)
					{
						stack.stackSize -= k - itemstack.stackSize;
						itemstack.stackSize = k;
						slot.onSlotChanged();
						flag = true;
					}
				}

				if (reverseDirection)
				{
					--i;
				}
				else
				{
					++i;
				}
			}
		}

		if (stack.stackSize > 0)
		{
			if (reverseDirection)
			{
				i = endIndex - 1;
			}
			else
			{
				i = startIndex;
			}

			while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)
			{
				Slot slot1 = inventorySlots.get(i);

				if (! slot1.getHasStack() && slot1.isItemValid(stack)) // Forge: Make sure to respect isItemValid in the slot.
				{
					ItemStack itemstack = stack.copy();
					int k = Math.min(slot1.getItemStackLimit(itemstack), itemstack.getMaxStackSize());

					if (k < itemstack.stackSize)
					{
						itemstack.stackSize = k;
						stack.stackSize = stack.stackSize -k;
					} else
					{
						stack.stackSize = 0;
					}

					slot1.putStack(itemstack);
					slot1.onSlotChanged();
					flag = true;

					if (0 < stack.stackSize)
					{
						if (reverseDirection) return flag || mergeItemStack(stack, startIndex, endIndex -i -1, reverseDirection);
						else return flag || mergeItemStack(stack, i +1, endIndex, reverseDirection);
					}

					break;
				}

				if (reverseDirection)
				{
					--i;
				}
				else
				{
					++i;
				}
			}
		}

		return flag;
	}

}
