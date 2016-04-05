package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * インベントリ付きTileEntity用の抽象クラス
 *
 * @author Kanomiya
 *
 */
public abstract class ITileEntityWithInventory extends TileEntity implements IInventory {
	protected ItemStack[] items;
	protected String customName;
	protected int numPlayersUsing;

	public ITileEntityWithInventory() {
		super();
		items = new ItemStack[getSizeInventory()];
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		if (tag.hasKey("CustomName", 8)) {
			customName = tag.getString("CustomName");
		}

		NBTTagCompound tagItems = tag.getCompoundTag("items");
		for (int i=0; i<items.length; i++) {
			items[i] = ItemStack.loadItemStackFromNBT(tagItems.getCompoundTag("slot_" +i));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		NBTTagCompound tagItems = tag.getCompoundTag("items");
		int i=0;
		for (ItemStack is: items) {
			if (is != null) {
				NBTTagCompound tagItem = tag.getCompoundTag("slot_" + i);
				is.writeToNBT(tagItem);
				tagItems.setTag("slot_" + i, tagItem);
			}

			i++;
		}

		tag.setTag("items", tagItems);

	}


	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
	// IInventory's Methods
	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--]

	@Override
	public boolean hasCustomName() { return customName != null && customName.length() > 0; }

	public void setCustomName(String name) { customName = name; }

	@Override
	public ITextComponent getDisplayName() {
		return hasCustomName() ? new TextComponentString(customName) : new TextComponentTranslation(getName(), new Object[0]);
	}

	@Override
	public ItemStack getStackInSlot(int index) { return items[index]; }

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (items[index] != null) {
			ItemStack itemstack;

			if (items[index].stackSize <= count) {
				itemstack = items[index];
				items[index] = null;
				markDirty();
				return itemstack;
			} else {
				itemstack = items[index].splitStack(count);

				if (items[index].stackSize == 0) {
					items[index] = null;
				}

				markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	/**
	* @inheritDoc
	*/
	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (items[index] != null) {
			ItemStack itemstack = items[index];
			items[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		items[index] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		markDirty();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)  {
		return worldObj.getTileEntity(pos) != this ? false : player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if (! player.isSpectator()) {
			if (numPlayersUsing < 0) {
				numPlayersUsing = 0;
			}

			++numPlayersUsing;
			worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
			worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
			worldObj.notifyNeighborsOfStateChange(pos.down(), getBlockType());
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (! player.isSpectator() && getBlockType() instanceof BlockChest) {
			--numPlayersUsing;
			worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
			worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
			worldObj.notifyNeighborsOfStateChange(pos.down(), getBlockType());
		}
	}

	@Override
	public int getField(int id) { return 0; }

	@Override
	public void setField(int id, int value) { }

	@Override
	public int getFieldCount() { return 0; }

	@Override
	public void clear() {
		for (int i = 0; i < items.length; ++i) {
			items[i] = null;
		}
	}

	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--
	// --*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--

	@Override public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return new SPacketUpdateTileEntity(pos, 1, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
}
