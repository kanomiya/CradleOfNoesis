package com.kanomiya.mcmod.cradleofnoesis.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
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
public class MessageMagicStatusEntity implements IMessage {

	public static class Handler implements IMessageHandler<MessageMagicStatusEntity, IMessage>
	{
		/**
		* @inheritDoc
		*/
		@Override
		public IMessage onMessage(MessageMagicStatusEntity message, MessageContext ctx)
		{
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityId);

			if (entity instanceof Entity && entity.hasCapability(CradleOfNoesisAPI.capMagicStatus, null))
			{
				entity.getCapability(CradleOfNoesisAPI.capMagicStatus, null).deserializeNBT(message.nbt);
			}
			/* else
			{
				new Thread("MagicStatusEntity-Delayed-Updater")
				{
					@Override
					public void run()
					{
						int maxRetry = 7;
						int retryCount = 0;

						while (retryCount < maxRetry)
						{
							if (entity instanceof Entity && entity.hasCapability(CradleOfNoesisAPI.capMagicStatus, null))
							{
								entity.getCapability(CradleOfNoesisAPI.capMagicStatus, null).deserializeNBT(message.nbt);
								break;
							} else ++ retryCount;

							try
							{
								Thread.sleep(500);
							} catch (InterruptedException e) { break; }
						}

					}
				}.start();;
			}*/

			return null;
		}

	}

	protected int entityId;
	protected NBTTagCompound nbt;

	public MessageMagicStatusEntity()
	{

	}

	public MessageMagicStatusEntity(Entity entity, MagicStatus<? extends ICapabilityProvider> magicStatus)
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
