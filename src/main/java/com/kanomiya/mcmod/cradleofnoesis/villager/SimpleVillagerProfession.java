package com.kanomiya.mcmod.cradleofnoesis.villager;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class SimpleVillagerProfession extends VillagerRegistry.VillagerProfession
{
	public SimpleVillagerProfession(ResourceLocation name, ResourceLocation texture)
	{
		super(name.toString(), texture.toString());
	}

}
