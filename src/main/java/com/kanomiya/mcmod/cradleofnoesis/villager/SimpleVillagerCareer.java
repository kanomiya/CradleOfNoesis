package com.kanomiya.mcmod.cradleofnoesis.villager;

import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class SimpleVillagerCareer extends VillagerRegistry.VillagerCareer
{
	protected ITradeList[][] trades;

	public SimpleVillagerCareer(VillagerRegistry.VillagerProfession parent, String name)
	{
		super(parent, name);
	}

	@Override
	public ITradeList[][] getTrades()
	{
		return trades;
	}

	public SimpleVillagerCareer init(ITradeList[][] trades)
	{
		this.trades = trades;
		return this;
	}

}
