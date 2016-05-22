package com.kanomiya.mcmod.cradleofnoesis.item;

import java.util.List;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySpawnerBall;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Kanomiya
 *
 */
public abstract class ItemEntitySpawner extends Item
{

	public ItemEntitySpawner()
	{
		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemEntitySpawner.EnumType[] values = ItemEntitySpawner.EnumType.values();
		if (itemStackIn.getMetadata() < values.length)
		{
			ItemEntitySpawner.EnumType type = values[itemStackIn.getMetadata()];

			switch (type)
			{
			case PLACER_AT: return placeEntityAt(itemStackIn, worldIn, playerIn, hand);
			case PLACER: return placeEntity(itemStackIn, worldIn, playerIn, hand);
			case BALL: return throwEntity(itemStackIn, worldIn, playerIn, hand);
			}

		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}

	public ActionResult<ItemStack> placeEntityAt(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		float f = 1.0F;
		float f1 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * f;
		float f2 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * f;
		double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * f;
		double d1 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * f + playerIn.getEyeHeight();
		double d2 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * f;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		Vec3d vec3d1 = vec3d.addVector(f7 * d3, f6 * d3, f8 * d3);
		RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);

		if (raytraceresult == null)
		{
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
		}
		else
		{
			Vec3d vec3d2 = playerIn.getLook(f);
			boolean flag = false;
			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().addCoord(vec3d2.xCoord * d3, vec3d2.yCoord * d3, vec3d2.zCoord * d3).expandXyz(1.0D));

			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity = list.get(i);

				if (entity.canBeCollidedWith())
				{
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expandXyz(entity.getCollisionBorderSize());

					if (axisalignedbb.isVecInside(vec3d))
					{
						flag = true;
					}
				}
			}

			if (flag)
			{
				return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
			}
			else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
			{
				return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
			}
			else
			{
				Entity entity = getEntity(itemStackIn, worldIn, playerIn, hand);

				if (entity != null)
				{
					entity.setLocationAndAngles(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord, playerIn.rotationYaw, entity.rotationPitch);

					if (!worldIn.getCollisionBoxes(entity, entity.getEntityBoundingBox().expandXyz(-0.1D)).isEmpty())
					{
						return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
					}
					else
					{
						if (!worldIn.isRemote)
						{
							worldIn.spawnEntityInWorld(entity);

							onEntitySpawn(itemStackIn, worldIn, playerIn, hand, entity);
						}

						if (!playerIn.capabilities.isCreativeMode)
						{
							--itemStackIn.stackSize;
						}

						playerIn.addStat(StatList.getObjectUseStats(this));
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
					}

				}
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}

	public ActionResult<ItemStack> placeEntity(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		Entity entity = getEntity(itemStackIn, worldIn, playerIn, hand);

		if (entity != null)
		{
			entity.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
			if (! worldIn.isRemote) worldIn.spawnEntityInWorld(entity);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}

	public ActionResult<ItemStack> throwEntity(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		Entity entity = getEntity(itemStackIn, worldIn, playerIn, hand);

		if (entity != null)
		{
			EntitySpawnerBall<EntitySanctuary> ball = new EntitySpawnerBall<>(worldIn, playerIn);
			ball.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 1.5f, 1.0f);

			ball.setSpawnEntity(entity);

			if (! worldIn.isRemote) worldIn.spawnEntityInWorld(ball);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}

	public abstract Entity getEntity(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand);

	public void onEntitySpawn(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand, Entity entityIn)
	{

	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		ItemEntitySpawner.EnumType[] values = ItemEntitySpawner.EnumType.values();
		return super.getUnlocalizedName(stack) + (stack.getMetadata() < values.length ? "_" + values[stack.getMetadata()].name().toLowerCase() : "");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		ItemEntitySpawner.EnumType[] values = ItemEntitySpawner.EnumType.values();

		for (int i=0; i<values.length; ++i)
		{
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	public static enum EnumType
	{
		PLACER_AT,
		PLACER,
		BALL,
		;

	}

}
