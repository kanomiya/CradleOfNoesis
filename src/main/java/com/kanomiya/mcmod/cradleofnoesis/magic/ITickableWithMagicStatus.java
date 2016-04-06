package com.kanomiya.mcmod.cradleofnoesis.magic;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

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
	 * You MUST call default methods to update MagicStatus.
	 *
	 * <p>
	 * {@link net.minecraft.item.Item#onUpdate(ItemStack, World, Entity, int, boolean)}: {@link ITickableWithMagicStatus.Item#onUpdate(ItemStack, World, Entity, int, boolean)}.<br>
	 *
	 * The code to call: ITickableWithMagicStatus.Item.super.onUpdate
	 *
	 * <p>
	 * {@link net.minecraft.item.Item#onEntityItemUpdate(EntityItem)}: {@link ITickableWithMagicStatus.Item#onEntityItemUpdate(EntityItem)}.<br>
	 *
	 * The code to call: ITickableWithMagicStatus.Item.super.onEntityItemUpdate
	 *
	 * @author Kanomiya
	 *
	 */
	public static interface Item extends IHasMagicStatus<ItemStack>
	{
		default void onUpdate(ItemStack stack, World worldIn, net.minecraft.entity.Entity entityIn, int itemSlot, boolean isSelected)
		{
			MagicStatus magicStatus = getMagicStatus(stack);

			if (magicStatus != null)
			{
				magicStatus.update();

				if (magicStatus.isUpdated())
				{
					PacketHandler.INSTANCE.sendToAll(new MessageMagicStatusHeldItem(entityIn, itemSlot, magicStatus));
					magicStatus.removeUpdatedFlag();
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
						magicStatus.removeUpdatedFlag();
					}

				}

			}

			return false;
		}

	}

	/**
	 *
	 * <p>
	 * You MUST call default methods to update MagicStatus.
	 *
	 * <p>
	 * {@link net.minecraft.entity.Entity#onUpdate()}: {@link ITickableWithMagicStatus.Entity#onUpdate()}.<br>
	 *
	 * The code to call: ITickableWithMagicStatus.Entity.super.onUpdate
	 *
	 * @author Kanomiya
	 *
	 */
	public static interface Entity extends IHasMagicStatus<net.minecraft.entity.Entity>
	{
		default void onUpdate()
		{
			if (this instanceof net.minecraft.entity.Entity)
			{
				MagicStatus magicStatus = getMagicStatus((net.minecraft.entity.Entity) this);
				if (magicStatus == null) return;

				magicStatus.update();
			}
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
	public static interface TileEntity extends IHasMagicStatus<net.minecraft.tileentity.TileEntity>, ITickable
	{
		@Override
		default void update()
		{
			if (this instanceof net.minecraft.tileentity.TileEntity)
			{
				MagicStatus magicStatus = getMagicStatus((net.minecraft.tileentity.TileEntity) this);
				if (magicStatus == null) return;

				magicStatus.update();
			}
		}

	}

}
