package com.kanomiya.mcmod.cradleofnoesis.magic.matter.type;

import java.util.Set;

import net.minecraft.util.ResourceLocation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.kanomiya.mcmod.cradleofnoesis.CONMagicMatterTypes;


/**
 * @author Kanomiya
 *
 */
public class MagicMatterTypeRegistry {
	public static final MagicMatterTypeRegistry INSTANCE = new MagicMatterTypeRegistry();

	protected BiMap<ResourceLocation, IMagicMatterType> idToType;

	protected MagicMatterTypeRegistry()
	{
		idToType = HashBiMap.create();
	}

	public void register(ResourceLocation id, IMagicMatterType iMagicMatterType)
	{
		idToType.put(id, iMagicMatterType);
	}

	public IMagicMatterType getTypeFromId(ResourceLocation id)
	{
		if (! idToType.containsKey(id)) return CONMagicMatterTypes.UNKNOWN;
		return idToType.get(id);
	}

	public ResourceLocation getIdFromType(IMagicMatterType iMagicMatterType)
	{
		if (! idToType.containsValue(iMagicMatterType) && iMagicMatterType != CONMagicMatterTypes.UNKNOWN) return getIdFromType(CONMagicMatterTypes.UNKNOWN);
		return idToType.inverse().get(iMagicMatterType);
	}


	public int size()
	{
		return idToType.size();
	}

	public Set<ResourceLocation> getIdSet()
	{
		return idToType.keySet();
	}

	public Set<IMagicMatterType> getTypeSet()
	{
		return idToType.values();
	}

}
