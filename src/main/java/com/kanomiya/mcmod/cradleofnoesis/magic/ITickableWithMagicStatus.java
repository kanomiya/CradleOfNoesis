package com.kanomiya.mcmod.cradleofnoesis.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.network.MessageMagicStatusEntityItem;
import com.kanomiya.mcmod.cradleofnoesis.network.MessageMagicStatusHeldItem;
import com.kanomiya.mcmod.cradleofnoesis.network.PacketHandler;


/**
 *
 * For Item / TileEntity
 *
 * @author Kanomiya
 *
 */
public abstract class ITickableWithMagicStatus {

	/**
	 *
	 * <p>
	 * You need Only to implement this for auto-updating MagicStatus.
	 *
	 * <p>
	 * If the Item class extends {@link net.minecraft.item.Item#onUpdate(ItemStack, World, Entity, int, boolean)},
	 * you MUST code the Item class to call {@link ITickableWithMagicStatus.Item#onUpdate(ItemStack, World, Entity, int, boolean)}.<br>
	 *
	 * The code to call: ITickableWithMagicStatus.Item.super.onUpdate
	 *
	 * <p>
	 * If the Item class extends {@link net.minecraft.item.Item#onEntityItemUpdate(EntityItem)},
	 * you MUST code the Item class to call {@link ITickableWithMagicStatus.Item#onEntityItemUpdate(EntityItem)}.<br>
	 *
	 * The code to call: ITickableWithMagicStatus.Item.super.onEntityItemUpdate
	 *
	 * @author Kanomiya
	 *
	 */
	public static interface Item
	{
		default MagicStatus getMagicStatus(ItemStack stack)
		{
			if (! stack.hasCapability(CradleOfNoesisAPI.capMagicStatus, null)) return null;
			return stack.getCapability(CradleOfNoesisAPI.capMagicStatus, null);
		}

		default void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
		{
			MagicStatus magicStatus = getMagicStatus(stack);

			if (magicStatus != null)
			{
				magicStatus.update();

				if (magicStatus.isUpdated())
				{
					PacketHandler.INSTANCE.sendToAll(new MessageMagicStatusHeldItem(entityIn, itemSlot, magicStatus));
					magicStatus.updated = false;
				}

			}

		}

		default boolean onEntityItemUpdate(EntityItem entityItem)
		{
			ItemStack stack = entityItem.getEntityItem();

			if (stack != null)
			{
				MagicStatus magicStatus = getMagicStatus(stack);

				if (magicStatus != null)
				{
					magicStatus.update();

					if (magicStatus.isUpdated())
					{
						PacketHandler.INSTANCE.sendToAll(new MessageMagicStatusEntityItem(entityItem, magicStatus));
						magicStatus.updated = false;
					}

				}

			}

			return false;
		}
	}

	/**
	 *
	 * <p>
	 * You need Only to implement this for auto-updating MagicStatus.
	 *
	 * <p>
	 * If the TileEntity class extends {@link net.minecraft.util.ITickable#update()},
	 * you MUST code the TileEntity class to call {@link ITickableWithMagicStatus.TileEntity#update()}.<br>
	 *
	 * The code to call: ITickableWithMagicStatus.TileEntity.super.onUpdate
	 *
	 * @author Kanomiya
	 *
	 */
	public static interface TileEntity extends ITickable
	{
		MagicStatus getMagicStatus();

		@Override
		default void update()
		{
			MagicStatus magicStatus = getMagicStatus();
			if (magicStatus == null) return;

			magicStatus.update();
		}

	}

}
