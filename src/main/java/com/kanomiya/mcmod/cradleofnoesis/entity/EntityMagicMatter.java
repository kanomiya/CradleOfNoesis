package com.kanomiya.mcmod.cradleofnoesis.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.magic.ITickableWithMagicStatus;

/**
 * @author Kanomiya
 *
 */
public class EntityMagicMatter extends Entity implements ITickableWithMagicStatus.Entity {

	/**
	 * @param worldIn
	 */
	public EntityMagicMatter(World worldIn) {
		super(worldIn);

		setSize(1f, 1f);
		preventEntitySpawning = true;

	}

	public EntityMagicMatter(World worldIn, double posX, double posY, double posZ)
	{
		this(worldIn);
		setPosition(posX, posY, posZ);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
	}

	/**
	* @inheritDoc
	*/
	@Override
	protected void entityInit()
	{

	}

	@Override
	public void onUpdate()
	{
		// super.onUpdate();
		ITickableWithMagicStatus.Entity.super.onUpdate();

		super.onUpdate();

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.03999999910593033D;
		noClip = pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);
		moveEntity(motionX, motionY, motionZ);
		boolean flag = (int)prevPosX != (int)posX || (int)prevPosY != (int)posY || (int)prevPosZ != (int)posZ;

		if (flag || ticksExisted % 25 == 0)
		{
			if (worldObj.getBlockState(new BlockPos(this)).getMaterial() == Material.lava)
			{
				motionY = 0.20000000298023224D;
				motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
				motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
				playSound(SoundEvents.entity_generic_burn, 0.4F, 2.0F + rand.nextFloat() * 0.4F);
			}

		}

		float f = 0.98F;
		if (onGround)
		{
			f = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.98F;
		}

		motionX *= f;
		motionY *= 0.9800000190734863D;
		motionZ *= f;

		if (onGround)
		{
			motionY *= -0.5D;
		}

		handleWaterMovement();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		super.attackEntityFrom(source, amount);

		if (! worldObj.isRemote && ! isDead)
		{
			if (worldObj.getGameRules().getBoolean("doEntityDrops"))
			{
				ItemStack drop = new ItemStack(CradleOfNoesis.CONItems.itemMagicMatter, 1, 0);
				entityDropItem(drop, 1.0f);
				setDead();

				return true;
			}

		}

		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return ! isDead;
	}

	@Override
	public boolean canBePushed()
	{
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return entityIn.getEntityBoundingBox();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}


	/**
	* @inheritDoc
	*/
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{

	}

	/**
	* @inheritDoc
	*/
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{

	}

}
