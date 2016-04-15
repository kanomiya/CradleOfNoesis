package com.kanomiya.mcmod.cradleofnoesis.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;

/**
 * @author Kanomiya
 *
 */
public class ItemIntelligentStone extends Item {

	public ItemIntelligentStone()
	{
		setRegistryName(new ResourceLocation(CradleOfNoesis.MODID, "itemIntelligentStone"));
		setUnlocalizedName("itemIntelligentStone");

		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{

	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{

		return false;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "_" + EnumIntelligentStoneColor.fromMeta(stack.getMetadata()).toColorName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
		subItems.add(new ItemStack(itemIn, 1, 2));
	}

	public static enum EnumIntelligentStoneColor
	{
		BLACK,
		WHITE,
		RED
		;


		public String toColorName()
		{
			switch(this)
			{
			case WHITE: return "white";
			case RED: return "red";
			case BLACK: return "black";
			}

			return "unknown";
		}

		public static EnumIntelligentStoneColor fromMeta(int meta)
		{
			switch (meta)
			{
			case 0: return BLACK;
			case 1: return WHITE;
			case 2: return RED;
			}
			return BLACK;
		}

	}





}
