package com.kanomiya.mcmod.cradleofnoesis.api;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuaryInfo;
import com.kanomiya.mcmod.cradleofnoesis.magic.IMagicNodeShape;
import com.kanomiya.mcmod.cradleofnoesis.magic.IMagicNodeShapeRender;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;


/**
 * @author Kanomiya
 *
 */
public class CradleOfNoesisAPI
{
	public static final String MODID = "com.kanomiya.mcmod.cradleofnoesis";

	public static final String DATAID_SANCTUARYSET = MODID + ":sanctuary";

	public static final ResourceLocation ENDER_ENERGY_LOCATION = new ResourceLocation(MODID, "enderEnergy");

	private static final BiMap<ResourceLocation, Class<? extends ISanctuary>> sanctuaryRegistry = HashBiMap.create();
	private static final Map<Class<? extends ISanctuary>, ISanctuaryInfo> sanctuaryInfoRegistry = Maps.newHashMap();

	public static final DataSerializer<Optional<ISanctuary>> SANCTUARY_DATASERIALIZER = new DataSerializer<Optional<ISanctuary>>()
	{
		@Override
		public void write(PacketBuffer buf, Optional<ISanctuary> value)
		{

			if (value.isPresent())
			{
				ResourceLocation id = sanctuaryRegistry.inverse().get(value.get().getClass());
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

				Optional<ISanctuary> optSanctuary = createSanctuaryInstance(id);;

				if (optSanctuary.isPresent())
				{
					NBTTagCompound nbt = buf.readNBTTagCompoundFromBuffer();
					optSanctuary.get().deserializeNBT(nbt);

					return optSanctuary;
				}

			}

			return Optional.absent();
		}

		@Override
		public DataParameter<Optional<ISanctuary>> createKey(int id)
		{
			return new DataParameter<Optional<ISanctuary>>(id, this);
		}

	};


	public static final Map<Class<IMagicNodeShape>, IMagicNodeShapeRender> magicNodeShapeRenderRegistry = Maps.newHashMap();


	static
	{
		DataSerializers.registerSerializer(SANCTUARY_DATASERIALIZER);

	}

	public static void registerSanctuary(ResourceLocation id, Class<? extends ISanctuary> clazz, ISanctuaryInfo info)
	{
		sanctuaryRegistry.put(id, clazz);
		sanctuaryInfoRegistry.put(clazz, info);
	}



	public static Set<ResourceLocation> getRegisteredSanctuaryIdSet()
	{
		return sanctuaryRegistry.keySet();
	}

	public static Set<Class<? extends ISanctuary>> getRegisteredSanctuaryClassSet()
	{
		return sanctuaryRegistry.values();
	}

	public static Optional<ISanctuaryInfo> getSanctuaryInfo(Class<? extends ISanctuary> clazz)
	{
		if (sanctuaryInfoRegistry.containsKey(clazz))
		{
			return Optional.of(sanctuaryInfoRegistry.get(clazz));
		}

		return Optional.absent();
	}

	public static Optional<ResourceLocation> getSanctuaryId(Class<? extends ISanctuary> clazz)
	{
		if (sanctuaryRegistry.containsValue(clazz))
		{
			return Optional.of(sanctuaryRegistry.inverse().get(clazz));
		}

		return Optional.absent();

	}

	public static Optional<ISanctuary> createSanctuaryInstance(ResourceLocation id)
{
		Class<? extends ISanctuary> clazz = sanctuaryRegistry.get(id);
		if (clazz != null)
		{
			try
			{
				return Optional.of(clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}

		return Optional.absent();
	}

	/**
	 *
	 * Need 'sanctuaryId' and 'sanctuary'
	 *
	 * @param nbt
	 * @return
	 */
	public static Optional<ISanctuary> deserializeSanctuary(NBTTagCompound nbt)
	{
		Optional<ISanctuary> optSanctuary = CradleOfNoesisAPI.createSanctuaryInstance(new ResourceLocation(nbt.getString("sanctuaryId")));

		if (optSanctuary.isPresent())
		{
			optSanctuary.get().deserializeNBT(nbt.getCompoundTag("sanctuary"));

			return optSanctuary;
		}

		return Optional.absent();
	}

	/**
	 *
	 * Return 'sanctuaryId' and 'sanctuary'
	 *
	 * @param sanctuary
	 * @return
	 */
	public static Optional<NBTTagCompound> serializeSanctuary(ISanctuary sanctuary)
	{
		ResourceLocation id = CradleOfNoesisAPI.sanctuaryRegistry.inverse().get(sanctuary.getClass());
		if (id != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();

			nbt.setString("sanctuaryId", id.toString());
			nbt.setTag("sanctuary", sanctuary.serializeNBT());

			return Optional.of(nbt);
		}

		return Optional.absent();
	}


	private CradleOfNoesisAPI() {  }
}
