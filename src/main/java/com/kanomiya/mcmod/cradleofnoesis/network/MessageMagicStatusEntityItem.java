package com.kanomiya.mcmod.cradleofnoesis.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
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
public class MessageMagicStatusEntityItem implements IMessage {

	public static class Handler implements IMessageHandler<MessageMagicStatusEntityItem, IMessage>
	{
		/**
		* @inheritDoc
		*/
		@Override
		public IMessage onMessage(MessageMagicStatusEntityItem message, MessageContext ctx)
		{
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityId);

			if (entity instanceof EntityItem)
			{
				EntityItem entityItem = (EntityItem) entity;
				ItemStack stack = entityItem.getEntityItem();

				if (stack != null && stack.hasCapability(CradleOfNoesisAPI.capMagicStatus, null))
				{
					stack.getCapability(CradleOfNoesisAPI.capMagicStatus, null).deserializeNBT(message.nbt);
				}
			}

			return null;
		}

	}

	protected int entityId;
	protected NBTTagCompound nbt;

	public MessageMagicStatusEntityItem()
	{

	}

	public MessageMagicStatusEntityItem(EntityItem entity, MagicStatus<? extends ICapabilityProvider> magicStatus)
	{
		entityId = entity.getEntityId();
		nbt = magicStatus.serializeNBT();
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void fromBytes(ByteBuf buf)
	{
		entityId = buf.readInt();
		nbt = ByteBufUtils.readTag(buf);
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityId);
		ByteBufUtils.writeTag(buf, nbt);
	}


}
