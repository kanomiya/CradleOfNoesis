package com.kanomiya.mcmod.cradleofnoesis;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityFlyPod;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySpawnerBall;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEmeraldTablet;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEntitySpawner;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;
import com.kanomiya.mcmod.cradleofnoesis.sanctuary.HealSanctuary;

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

	public static Item itemHealSanctuary = new ItemEntitySpawner()
	{
		@Override
		public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
		{
			if (itemStackIn.getMetadata() == 1)
			{
				Entity ball = getEntity(itemStackIn, worldIn, playerIn, hand);

				if (ball != null)
				{
					if (! worldIn.isRemote) worldIn.spawnEntityInWorld(ball);
					return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
				}

			} else
			{
				return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
			}

			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}

		@Override
		public Entity getEntity(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
		{
			EntitySanctuary entity = new EntitySanctuary(worldIn);
			ISanctuary sanctuary = new HealSanctuary(5.0f, 500, 0.1f, 5);

			sanctuary.allowToEnter(playerIn);
			entity.setSanctuary(sanctuary);

			if (itemStackIn.getMetadata() == 1)
			{
				EntitySpawnerBall<EntitySanctuary> ball = new EntitySpawnerBall<>(worldIn, playerIn);
				ball.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 1.5f, 1.0f);

				ball.setSpawnEntity(entity);

				return ball;
			}

			return entity;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
		{
			subItems.add(new ItemStack(itemIn, 1, 0));
			subItems.add(new ItemStack(itemIn, 1, 1));
		}

	}
		.setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemHealSanctuary"))
		.setUnlocalizedName("itemHealSanctuary")
		.setCreativeTab(CradleOfNoesis.tab);


}
