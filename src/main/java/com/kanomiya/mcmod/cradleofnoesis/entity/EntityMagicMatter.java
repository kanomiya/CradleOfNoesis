package com.kanomiya.mcmod.cradleofnoesis.entity;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import com.google.common.base.Optional;
import com.kanomiya.mcmod.cradleofnoesis.CONItems;
import com.kanomiya.mcmod.cradleofnoesis.CONMagicMatterTypes;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.state.MagicMatterForm;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.type.IMagicMatterType;

/**
 * @author Kanomiya
 *
 */
public class EntityMagicMatter extends Entity
{
	private static final DataParameter<Optional<ItemStack>> MATTER_STACK = EntityDataManager.<Optional<ItemStack>>createKey(EntityMagicMatter.class, DataSerializers.OPTIONAL_ITEM_STACK);

	/**
	 * @param worldIn
	 */
	public EntityMagicMatter(World worldIn) {
		super(worldIn);

		setPosition(0, 0, 0);
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
		dataWatcher.register(MATTER_STACK, Optional.of(new ItemStack(CONItems.itemMagicMatter, 1, 0)));
	}

	@Override
	public void onUpdate()
	{
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
				MagicStatus magicStatus = getStackMagicStatus();

				if (magicStatus != null)
				{
					Random rand = new Random();

					if (rand.nextFloat() <= magicStatus.getMatter().getDropChance())
					{
						ItemStack drop = new ItemStack(CONItems.itemMagicMatter, 1, 0);
						if (drop.hasCapability(CradleOfNoesisAPI.capMagicStatus, null))
						{
							drop.getCapability(CradleOfNoesisAPI.capMagicStatus, null).deserializeNBT(magicStatus.serializeNBT());
						}

						entityDropItem(drop, 1.0f);
					}

				}
			}

			setDead();
			return true;
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
		return getEntityBoundingBox().expand(-0.1d, 0, -0.1d);
	}


	public boolean hasMatterStack()
	{
		return dataWatcher.get(MATTER_STACK).isPresent();
	}

	public ItemStack getMatterStack()
	{
		return dataWatcher.get(MATTER_STACK).get();
	}

	public void setMatterStack(ItemStack stack)
	{
		dataWatcher.set(MATTER_STACK, Optional.of(stack));
	}

	public MagicStatus<ItemStack> getStackMagicStatus()
	{
		return hasMatterStack() && getMatterStack().hasCapability(CradleOfNoesisAPI.capMagicStatus, null) ? getMatterStack().getCapability(CradleOfNoesisAPI.capMagicStatus, null) : null;
	}

	public IMagicMatterType getType()
	{
		MagicStatus magicStatus = getStackMagicStatus();
		if (magicStatus == null || ! magicStatus.hasMatter()) return CONMagicMatterTypes.UNKNOWN;
		return magicStatus.getMatter().getMatterType();
	}

	public MagicMatterForm getForm()
	{
		MagicStatus magicStatus = getStackMagicStatus();
		if (magicStatus == null || ! magicStatus.hasMatter()) return MagicMatterForm.BLOCK;
		return magicStatus.getMatter().getMatterState().getForm();
	}

	// EntityBoat

	/**
	* @inheritDoc
	*/
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("matterStack", NBT.TAG_COMPOUND)) setMatterStack(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("matterStack")));
	}

	/**
	* @inheritDoc
	*/
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		if (hasMatterStack()) nbt.setTag("matterStack", getMatterStack().serializeNBT());
	}

}
