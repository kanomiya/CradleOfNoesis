package com.kanomiya.mcmod.cradleofnoesis.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.util.EnderEnergyUtils;

/**
 * @author Kanomiya
 *
 */
public class ItemIntelligentStone extends Item
{
	public ItemIntelligentStone()
	{
		setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemIntelligentStone"));
		setUnlocalizedName("itemIntelligentStone");

		setCreativeTab(CradleOfNoesis.tab);
		setHasSubtypes(true);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{


		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return 0 < EnderEnergyUtils.getEnderEnergyCapacity(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1.0d -(double)EnderEnergyUtils.getEnderEnergyAmount(stack) /(double)EnderEnergyUtils.getEnderEnergyCapacity(stack);
	}


	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		EnumType[] values = EnumType.values();
		return super.getUnlocalizedName(stack) + (stack.getMetadata() < values.length ? "_" + values[stack.getMetadata()].name().toLowerCase() : "");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		EnumType[] values = EnumType.values();

		for (int i=0; i<values.length; ++i)
		{
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	public static enum EnumType
	{
		BLACK,
		WHITE,
		RED,
		;

	}





}
