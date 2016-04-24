package com.kanomiya.mcmod.cradleofnoesis;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityFlyPod;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEmeraldTablet;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEntitySpawner;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemInstantSanctuary;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemSanctuaryRemover;

public class CONItems
{
	public static ItemIntelligentStone itemIntelligentStone = new ItemIntelligentStone();

	public static Item itemYuleIngot = new Item()
		.setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemYuleIngot"))
		.setUnlocalizedName("itemYuleIngot")
		.setCreativeTab(CradleOfNoesis.tab);

	public static Item itemTsafaIngot = new Item()
		.setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemTsafaIngot"))
		.setUnlocalizedName("itemTsafaIngot")
		.setCreativeTab(CradleOfNoesis.tab);

	public static Item itemRanimIngot = new Item()
		.setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemRanimIngot"))
		.setUnlocalizedName("itemRanimIngot")
		.setCreativeTab(CradleOfNoesis.tab);

	public static ItemEmeraldTablet itemEmeraldTablet = new ItemEmeraldTablet();

	public static Item itemFlyPod = new ItemEntitySpawner()
	{
		@Override
		public Entity getEntity(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
		{
			return new EntityFlyPod(worldIn);
		}
	}
		.setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemFlyPod"))
		.setUnlocalizedName("itemFlyPod")
		.setCreativeTab(CradleOfNoesis.tab);

	public static Item itemInstantSanctuary = new ItemInstantSanctuary();

	public static ItemSanctuaryRemover itemSanctuaryRemover = new ItemSanctuaryRemover();

}
