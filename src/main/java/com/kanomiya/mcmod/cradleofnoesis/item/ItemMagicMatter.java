package com.kanomiya.mcmod.cradleofnoesis.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityMagicMatter;
import com.kanomiya.mcmod.cradleofnoesis.magic.ITickableWithMagicStatus;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.MagicMatter;

/**
 * @author Kanomiya
 *
 */
public class ItemMagicMatter extends Item implements ITickableWithMagicStatus.Item {

	public ItemMagicMatter()
	{
		setRegistryName(new ResourceLocation(CradleOfNoesis.MODID, "itemMagicMatter"));
		setUnlocalizedName("itemMagicMatter");

		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		ITickableWithMagicStatus.Item.super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		ITickableWithMagicStatus.Item.super.onEntityItemUpdate(entityItem);

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		subItems.add(new ItemStack(itemIn, 1, 0));

		NBTTagCompound nbt = new NBTTagCompound();
		MagicMatter magicMatter = new MagicMatter();

		nbt.setTag(CradleOfNoesis.MODID + ":defaultMagicMatter", magicMatter.serializeNBT());

		subItems.add(new ItemStack(itemIn, 1, 0, nbt));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		MagicStatus magicStatus = getMagicStatus(stack);
		if (magicStatus != null)
		{
			tooltip.add("MP: " + magicStatus.getMp() + "/" + magicStatus.getMpCapacity());

			if (magicStatus.hasMatter())
			{
				tooltip.add("Matter: " + magicStatus.getMatter().getClass().getSimpleName());
			}

		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
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
			return new ActionResult(EnumActionResult.PASS, itemStackIn);
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
				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
			else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
			{
				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
			else
			{
				Block block = worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
				boolean flag1 = block == Blocks.water || block == Blocks.flowing_water;

				EntityMagicMatter entity = new EntityMagicMatter(worldIn, raytraceresult.hitVec.xCoord, flag1 ? raytraceresult.hitVec.yCoord - 0.12D : raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
				entity.rotationYaw = playerIn.rotationYaw;

				MagicStatus<ItemStack> magicStatusItem = getMagicStatus(itemStackIn);
				MagicStatus<Entity> magicStatusEntity = entity.getMagicStatus(entity);
				if (magicStatusItem != null && magicStatusEntity != null)
				{
					magicStatusEntity.deserializeNBT(magicStatusItem.serializeNBT());
				}

				if (!worldIn.getCubes(entity, entity.getEntityBoundingBox().expandXyz(-0.1D)).isEmpty())
				{
					return new ActionResult(EnumActionResult.FAIL, itemStackIn);
				}
				else
				{
					if (!worldIn.isRemote)
					{
						worldIn.spawnEntityInWorld(entity);
					}

					if (!playerIn.capabilities.isCreativeMode)
					{
						--itemStackIn.stackSize;
					}

					playerIn.addStat(StatList.func_188057_b(this));
					return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
				}
			}
		}
	}

}
