package com.kanomiya.mcmod.cradleofnoesis.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;
import com.kanomiya.mcmod.cradleofnoesis.sanctuary.HealSanctuary;

/**
 * @author Kanomiya
 *
 */
public class ItemSanctuary extends ItemEntitySpawner
{
	public ItemSanctuary()
	{
		super();

		setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemSanctuary"));
		setUnlocalizedName("itemSanctuary");
		setCreativeTab(CradleOfNoesis.tab);

	}

	@Override
	public Entity getEntity(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		EntitySanctuary entity = new EntitySanctuary(worldIn);
		ISanctuary sanctuary = new HealSanctuary(5.0f, 500, 0.1f, 5);

		sanctuary.allowToEnter(playerIn);
		entity.setSanctuary(sanctuary);

		return entity;
	}



}

