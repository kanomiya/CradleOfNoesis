package com.kanomiya.mcmod.cradleofnoesis.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.kanomiya.mcmod.cradleofnoesis.entity.EntityMagicMatter;

/**
 * @author Kanomiya
 *
 */
public class MessageEntityMagicMatter implements IMessage {

	public static class Handler implements IMessageHandler<MessageEntityMagicMatter, IMessage>
	{
		/**
		* @inheritDoc
		*/
		@Override
		public IMessage onMessage(MessageEntityMagicMatter message, MessageContext ctx)
		{
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityId);

			if (entity instanceof EntityMagicMatter)
			{
				((EntityMagicMatter) entity).setMatterStack(message.matterStack);
			}

			return null;
		}

	}

	protected int entityId;
	protected ItemStack matterStack;

	public MessageEntityMagicMatter()
	{

	}

	public MessageEntityMagicMatter(Entity entity, ItemStack matterStack)
	{
		entityId = entity.getEntityId();
		this.matterStack = matterStack;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void fromBytes(ByteBuf buf)
	{
		entityId = buf.readInt();
		matterStack = ByteBufUtils.readItemStack(buf);
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityId);
		ByteBufUtils.writeItemStack(buf, matterStack);
	}


}
