package com.kanomiya.mcmod.cradleofnoesis.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;

/**
 * @author Kanomiya
 *
 */
public class MessageMagicStatusHeldItem implements IMessage {

	public static class Handler implements IMessageHandler<MessageMagicStatusHeldItem, IMessage>
	{

		protected void update(IInventory inv, int slotNum, NBTTagCompound nbt)
		{
			if (inv.getSizeInventory() <= slotNum) return;

			ItemStack stack = inv.getStackInSlot(slotNum);

			if (stack != null && stack.hasCapability(CradleOfNoesisAPI.capMagicStatus, null))
			{
				stack.getCapability(CradleOfNoesisAPI.capMagicStatus, null).deserializeNBT(nbt);
			}

		}

		/**
		* @inheritDoc
		*/
		@Override
		public IMessage onMessage(MessageMagicStatusHeldItem message, MessageContext ctx)
		{
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityId);

			// if (entity instanceof EntityHorse); private inventory

			if (entity instanceof EntityPlayer)
			{
				IInventory inv = ((EntityPlayer) entity).inventory;
				update(inv, message.slotNum, message.nbt);
			}

			if (entity instanceof EntityVillager)
			{
				IInventory inv = ((EntityVillager) entity).getVillagerInventory();
				update(inv, message.slotNum, message.nbt);
			}

			return null;
		}

	}

	protected int entityId;
	protected int slotNum;
	protected NBTTagCompound nbt;

	public MessageMagicStatusHeldItem()
	{

	}

	public MessageMagicStatusHeldItem(Entity holder, int slotNum, MagicStatus<? extends ICapabilityProvider> magicStatus)
	{
		entityId = holder.getEntityId();
		this.slotNum = slotNum;
		nbt = magicStatus.serializeNBT();
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void fromBytes(ByteBuf buf)
	{
		entityId = buf.readInt();
		slotNum = buf.readInt();
		nbt = ByteBufUtils.readTag(buf);
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityId);
		buf.writeInt(slotNum);
		ByteBufUtils.writeTag(buf, nbt);
	}


}
