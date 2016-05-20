package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

import com.google.common.collect.Lists;
import com.kanomiya.mcmod.cradleofnoesis.CONItems;
import com.kanomiya.mcmod.cradleofnoesis.api.util.EnderEnergyUtils;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEmeraldTablet;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;

/**
 * @author Kanomiya
 *
 */
public class TileEntityLiaAlter extends ITileEntityWithInventory implements ITickable
{
	public static final int SLOT_MATERIAL = 0;
	public static final int SLOT_FUEL1 = 1;
	public static final int SLOT_FUEL2 = 2;
	public static final int SLOT_OFFERING_KNOWLEDGE = 3;
	public static final int SLOT_OFFERING2 = 4;

	private int brewingTime;
	private int brewingTimeInterval;

	protected List<ItemStack> consumingStacks;

	public TileEntityLiaAlter()
	{
		consumingStacks = Lists.newArrayList();
		brewingTime = 0;
		brewingTimeInterval = 100;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void update()
	{
		ItemStack material = getStackInSlot(SLOT_MATERIAL);
		ItemStack fuel1 = getStackInSlot(SLOT_FUEL1);
		ItemStack fuel2 = getStackInSlot(SLOT_FUEL2);
		ItemStack offeringKnowledge = getStackInSlot(SLOT_OFFERING_KNOWLEDGE);
		ItemStack offering2 = getStackInSlot(SLOT_OFFERING2);

		if (material != null && brewingTime == 0)
		{
			if (material.getItem() == Items.EGG) brewingTimeInterval = 500;
			else if (material.getItem() == CONItems.itemIntelligentStone)
			{
				int meta = material.getMetadata();

				if (meta == ItemIntelligentStone.EnumType.BLACK.ordinal()) brewingTimeInterval = 3000;
				else if (meta == ItemIntelligentStone.EnumType.WHITE.ordinal()) brewingTimeInterval = 10000;
				else brewingTimeInterval = 0;

			} else
			{
				brewingTimeInterval = 0;
			}
		}


		if (fuel1 != null)
		{
			int current = EnderEnergyUtils.getEnderEnergyAmount(fuel1);

			if (0 < current)
			{
				if (material != null)
				{
					EnderEnergyUtils.setEnderEnergyAmount(fuel1, current -1);
					if (0 < brewingTimeInterval) ++brewingTime;
				}
			} else
			{
				setInventorySlotContents(SLOT_FUEL1, null);
				worldObj.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
		}

		if (fuel2 != null)
		{
			int current = EnderEnergyUtils.getEnderEnergyAmount(fuel2);

			if (0 < current)
			{
				if (material != null)
				{
					EnderEnergyUtils.setEnderEnergyAmount(fuel2, current -1);
					if (0 < brewingTimeInterval) ++brewingTime;
				}
			} else
			{
				setInventorySlotContents(SLOT_FUEL2, null);
				worldObj.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
		}



		if (material != null)
		{
			if (brewingTimeInterval <= brewingTime)
			{
				if (offeringKnowledge != null)
				{
					if (offeringKnowledge.getItem() == CONItems.itemEmeraldTablet)
					{
						int current = EnderEnergyUtils.getEnderEnergyAmount(offeringKnowledge);

						if (brewingTimeInterval <= current)
						{
							EnderEnergyUtils.setEnderEnergyAmount(offeringKnowledge, current -brewingTimeInterval);



							ItemEmeraldTablet.EnumType[] values = ItemEmeraldTablet.EnumType.values();
							if (offeringKnowledge.getMetadata() < values.length)
							{
								ItemEmeraldTablet.EnumType type = values[offeringKnowledge.getMetadata()];

								switch (type)
								{
								case LIA_FALIA:
									if (material.getItem() == Items.EGG)
										setInventorySlotContents(SLOT_MATERIAL, new ItemStack(CONItems.itemIntelligentStone, 1, ItemIntelligentStone.EnumType.BLACK.ordinal()));
									break;
								case LIA_STILIA:
									if (material.getItem() == CONItems.itemIntelligentStone && material.getMetadata() == ItemIntelligentStone.EnumType.BLACK.ordinal())
										setInventorySlotContents(SLOT_MATERIAL, new ItemStack(CONItems.itemIntelligentStone, 1, ItemIntelligentStone.EnumType.WHITE.ordinal()));
									break;
								case LIA_REGILIA:
									if (material.getItem() == CONItems.itemIntelligentStone && material.getMetadata() == ItemIntelligentStone.EnumType.WHITE.ordinal())
										setInventorySlotContents(SLOT_MATERIAL, new ItemStack(CONItems.itemIntelligentStone, 1, ItemIntelligentStone.EnumType.RED.ordinal()));
									break;
								default:
									worldObj.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3.5f, true);
									break;
								}
							}


						}

						if (current <= 0)
						{
							setInventorySlotContents(SLOT_OFFERING_KNOWLEDGE, null);
							worldObj.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
						}

					} else
					{
						worldObj.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 2.0f, true);
					}
				} else
				{
					worldObj.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 2.0f, true);
				}

				worldObj.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
				brewingTime = 0;
			}

		} else
		{
			brewingTime = 0;
		}

	}


	/**
	 * @return if it has two ender pearls, true
	 */
	public boolean isBrewing()
	{
		return brewingTime > 0;
	}

	@Override
	public int getField(int id)
	{
		switch (id)
		{
			case 0:
				return brewingTime;
			case 1:
				return brewingTimeInterval;
		}

		if (getStackInSlot(SLOT_FUEL1) != null)
		{
			if (id == 2) return EnderEnergyUtils.getEnderEnergyAmount(getStackInSlot(SLOT_FUEL1));
			if (id == 3) return EnderEnergyUtils.getEnderEnergyCapacity(getStackInSlot(SLOT_FUEL1));
		}
		if (getStackInSlot(SLOT_FUEL2) != null)
		{
			if (id == 4) return EnderEnergyUtils.getEnderEnergyAmount(getStackInSlot(SLOT_FUEL2));
			if (id == 5) return EnderEnergyUtils.getEnderEnergyCapacity(getStackInSlot(SLOT_FUEL2));
		}

		if (getStackInSlot(SLOT_OFFERING_KNOWLEDGE) != null)
		{
			if (id == 6) return EnderEnergyUtils.getEnderEnergyAmount(getStackInSlot(SLOT_OFFERING_KNOWLEDGE));
			if (id == 7) return EnderEnergyUtils.getEnderEnergyCapacity(getStackInSlot(SLOT_OFFERING_KNOWLEDGE));
		}

		if (getStackInSlot(SLOT_OFFERING2) != null)
		{
			if (id == 8) return EnderEnergyUtils.getEnderEnergyAmount(getStackInSlot(SLOT_OFFERING2));
			if (id == 9) return EnderEnergyUtils.getEnderEnergyCapacity(getStackInSlot(SLOT_OFFERING2));
		}

		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
			case 0:
				brewingTime = value;
				break;
			case 1:
				brewingTimeInterval = value;
				break;
		}

		if (getStackInSlot(SLOT_FUEL1) != null)
		{
			if (id == 2) EnderEnergyUtils.setEnderEnergyAmount(getStackInSlot(SLOT_FUEL1), value);
			if (id == 3) EnderEnergyUtils.setEnderEnergyCapacity(getStackInSlot(SLOT_FUEL1), value);
		}
		if (getStackInSlot(SLOT_FUEL2) != null)
		{
			if (id == 4) EnderEnergyUtils.setEnderEnergyAmount(getStackInSlot(SLOT_FUEL2), value);
			if (id == 5) EnderEnergyUtils.setEnderEnergyCapacity(getStackInSlot(SLOT_FUEL2), value);
		}

		if (getStackInSlot(SLOT_OFFERING_KNOWLEDGE) != null)
		{
			if (id == 6) EnderEnergyUtils.setEnderEnergyAmount(getStackInSlot(SLOT_OFFERING_KNOWLEDGE), value);
			if (id == 7) EnderEnergyUtils.setEnderEnergyCapacity(getStackInSlot(SLOT_OFFERING_KNOWLEDGE), value);
		}

		if (getStackInSlot(SLOT_OFFERING2) != null)
		{
			if (id == 8) EnderEnergyUtils.setEnderEnergyAmount(getStackInSlot(SLOT_OFFERING2), value);
			if (id == 9) EnderEnergyUtils.setEnderEnergyCapacity(getStackInSlot(SLOT_OFFERING2), value);
		}

	}

	@Override
	public int getFieldCount()
	{
		return 10;
	}


	/**
	* @inheritDoc
	*/
	@Override
	public int getSizeInventory() {
		return 5;
	}


	/**
	* @inheritDoc
	*/
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0) return stack.getItem() == Items.EGG || stack.getItem() == CONItems.itemIntelligentStone;
		if (1 <= index && index <= 2) return stack.getItem() == Items.ENDER_PEARL;
		if (3 <= index && index <= 4) return stack.getItem() == CONItems.itemEmeraldTablet;
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
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);

		return nbt;
	}



}
