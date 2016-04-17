package com.kanomiya.mcmod.cradleofnoesis;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import com.kanomiya.mcmod.cradleofnoesis.entity.EntityFlyPod;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEmeraldTablet;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEntitySpawner;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;

public class CONItems
{
	public static ItemIntelligentStone itemIntelligentStone = new ItemIntelligentStone();

	public static Item itemYuleIngot = new Item()
		.setRegistryName(new ResourceLocation(CradleOfNoesis.MODID, "itemYuleIngot"))
		.setUnlocalizedName("itemYuleIngot")
		.setCreativeTab(CradleOfNoesis.tab);

	public static Item itemTsafaIngot = new Item()
		.setRegistryName(new ResourceLocation(CradleOfNoesis.MODID, "itemTsafaIngot"))
		.setUnlocalizedName("itemTsafaIngot")
		.setCreativeTab(CradleOfNoesis.tab);

	public static ItemEmeraldTablet itemEmeraldTablet = new ItemEmeraldTablet();

	public static Item itemFlyPodSpawner = new ItemEntitySpawner<EntityFlyPod>(EntityFlyPod::new)
		.setRegistryName(new ResourceLocation(CradleOfNoesis.MODID, "itemFlyPod"))
		.setUnlocalizedName("itemFlyPod")
		.setCreativeTab(CradleOfNoesis.tab);


}
