package com.kanomiya.mcmod.cradleofnoesis.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;

/**
 * @author Kanomiya
 *
 */
public class ItemSanctuaryRemover extends Item
{
	public ItemSanctuaryRemover()
	{
		setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemSanctuaryRemover"));
		setUnlocalizedName("itemSanctuaryRemover");

		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		List<Entity> entityList = worldIn.getEntitiesWithinAABB(EntitySanctuary.class, playerIn.getEntityBoundingBox().expandXyz(5d));

		for (Entity entity: entityList)
		{
			entity.setDead();
		}

		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

}
