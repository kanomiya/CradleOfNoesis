package com.kanomiya.mcmod.cradleofnoesis.api;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;


/**
 * @author Kanomiya
 *
 */
public class CradleOfNoesisAPI
{
	public static final String MODID = "com.kanomiya.mcmod.cradleofnoesis";

	public static final ResourceLocation ENDER_ENERGY_LOCATION = new ResourceLocation(MODID, "enderEnergy");

	public static final BiMap<ResourceLocation, Class<? extends ISanctuary>> SANCUTUARY_REGISTRY = HashBiMap.create();


	public static final DataSerializer<Optional<ISanctuary>> SANCTUARY_DATASERIALIZER = new DataSerializer<Optional<ISanctuary>>()
	{
		@Override
		public void write(PacketBuffer buf, Optional<ISanctuary> value)
		{

			if (value.isPresent())
			{
				ResourceLocation id = SANCUTUARY_REGISTRY.inverse().get(value.get().getClass());
				boolean exists = id != null;
				buf.writeBoolean(exists);

				if (exists)
				{
					String idStr = id.toString();

					buf.writeInt(idStr.length());
					buf.writeString(idStr);
					buf.writeNBTTagCompoundToBuffer(value.get().serializeNBT());

				}

			} else
			{
				buf.writeBoolean(false);
			}

		}

		@Override
		public Optional<ISanctuary> read(PacketBuffer buf) throws IOException
		{
			boolean exists = buf.readBoolean();

			if (exists)
			{
				int idStrLen = buf.readInt();
				ResourceLocation id = new ResourceLocation(buf.readStringFromBuffer(idStrLen));

				ISanctuary instance = createSanctuaryInstance(id);;

				if (instance != null)
				{
					NBTTagCompound nbt = buf.readNBTTagCompoundFromBuffer();
					instance.deserializeNBT(nbt);

					return Optional.of(instance);
				}

			}

			return Optional.absent();
		}

		@Override
		public DataParameter<Optional<ISanctuary>> createKey(int id)
		{
			return new DataParameter(id, this);
		}

	};

	static
	{
		DataSerializers.registerSerializer(SANCTUARY_DATASERIALIZER);

	}


	public static ISanctuary createSanctuaryInstance(ResourceLocation id)
{
		Class<? extends ISanctuary> clazz = SANCUTUARY_REGISTRY.get(id);
		if (clazz != null)
		{
			try
			{
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}


}
