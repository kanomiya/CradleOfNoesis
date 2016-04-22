package com.kanomiya.mcmod.cradleofnoesis.entity;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import com.google.common.base.Optional;
import com.kanomiya.mcmod.cradleofnoesis.CONItems;


/**
 * @author Kanomiya
 *
 */
public class EntityFlyPod extends EntityLiving implements IEntityOwnable
{
	protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityTameable.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	protected static final DataParameter<Optional<ItemStack>> STONE = EntityDataManager.createKey(EntityTameable.class, DataSerializers.OPTIONAL_ITEM_STACK);
	protected int fireCount;
	protected int fireInterval;

	protected int chatCount;
	protected int chatInterval;

	/**
	 * @param worldIn
	 */
	public EntityFlyPod(World worldIn)
	{
		super(worldIn);
		setSize(1.0f, 1.0f);

		fireInterval = 40;
		chatInterval = 1000;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(OWNER_UNIQUE_ID, Optional.<UUID>absent());
		dataManager.register(STONE, Optional.<ItemStack>absent());
	}

	@Override
	public void onLivingUpdate()
	{
		fallDistance = 0f;

		Entity owner = getOwner();

		if (getStoneStack() != null && getOwner() != null)
		{
			if (! worldObj.getCollisionBoxes(this, new AxisAlignedBB(posX, posY -2.5d, posZ, posX, posY, posZ)).isEmpty())
			{
				motionY = 0.1d; // EntityWolf
			}

			/*
			int d = 5;

			for (int x=-d; x<=d; ++x)
			{
				for (int y=-d; y<=d; ++y)
				{
					for (int z=-d; z<=d; ++z)
					{
						BlockPos blockPos = new BlockPos(posX +x, posY +y, posZ +z);
						IBlockState blockState = worldObj.getBlockState(blockPos);

						if (blockState != null)
						{
							if (blockState.isFullBlock()
									|| blockState.getBlock() == Blocks.FIRE
									|| blockState.getBlock() == Blocks.LAVA
									|| blockState.getBlock() == Blocks.FLOWING_LAVA)
							{
								Vec3d vecTracking = new Vec3d(x, y, z);
								vecTracking = vecTracking.scale(1d / vecTracking.lengthVector()); // 単位ベクトル

								vecTracking = vecTracking.scale(0.5d);

								motionX -= vecTracking.xCoord;
								motionZ -= vecTracking.zCoord;
								break;
							}
						}
					}
				}
			}
			*/


			Entity tracking = getAttackTarget() == null ? owner : getAttackTarget();

			if (tracking != null)
			{
				double dx = posX -tracking.posX;
				double dy = posY -tracking.posY;
				double dz = posZ -tracking.posZ;
				rotationYaw = (float) Math.toDegrees(Math.atan2(dx, dz));

				double r = Math.sqrt(dx*dx + dz*dz);
				rotationPitch = (float) Math.toDegrees(Math.atan2(dy, r));



				Vec3d vecTracking = tracking.getPositionVector().addVector(0, tracking.getEyeHeight(), 0);
				double hdist = getDistance(vecTracking.xCoord, posY, vecTracking.zCoord);
				double vdist = vecTracking.yCoord -posY;

				double hmargin = tracking == owner ? 4.5d : 16.0d;
				double vmargin = 0.5d;

				Vec3d vecArrow = vecTracking.subtract(getPositionVector());
				vecArrow = vecArrow.scale(1 /vecArrow.lengthVector()).scale(getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());

				if (hmargin < hdist)
				{
					motionX += vecArrow.xCoord;
					motionZ += vecArrow.zCoord;
				} else
				{
					// motionX *= 0.005d;
					// motionZ *= 0.005d;
					motionX -= vecArrow.xCoord;
					motionZ -= vecArrow.zCoord;
				}

				if (vmargin < vdist)
				{
					motionY += vecArrow.yCoord;
				}

				++chatCount;
				if (chatInterval < chatCount)
				{
					int i = rand.nextInt(1);
					if (! worldObj.isRemote)
					{
						owner.addChatMessage(new TextComponentString(getName() + ": " + I18n.translateToLocalFormatted("entity." + EntityList.getEntityString(this) + ".chat.passive_" + i, getName())));
					}

					chatCount = 0;
				}


				if (getAttackTarget() == null || getAttackTarget().isDead)
				{
					List<Entity> trackingList = worldObj.getEntitiesWithinAABBExcludingEntity(owner, getEntityBoundingBox().expandXyz(16.0d));

					for (Entity entity: trackingList) // EntityWolf
					{
						if (entity instanceof EntityLivingBase && entity != this && ! entity.isOnSameTeam(this) && canEntityBeSeen(entity))
						{
							if (entity instanceof IEntityOwnable && ((IEntityOwnable) entity).getOwner() == owner) continue;

							if (! worldObj.isRemote)
							{
								if (getAttackTarget() == null) owner.addChatMessage(new TextComponentString(getName() + ": " + I18n.translateToLocalFormatted("entity." + EntityList.getEntityString(this) + ".chat.findEnemy", getName())));
								else owner.addChatMessage(new TextComponentString(getName() + ": " + I18n.translateToLocalFormatted("entity." + EntityList.getEntityString(this) + ".chat.continueAttack", getName())));
							}

							setAttackTarget((EntityLivingBase) entity);
							break;
						}
					}

				}

			}

			if (getAttackTarget() != null)
			{
				tracking = getAttackTarget();

				if (tracking != this && ! tracking.isDead && canEntityBeSeen(tracking) && !(tracking instanceof IEntityOwnable && ((IEntityOwnable) tracking).getOwner() == owner))
				{
					++fireCount;

					if (fireInterval < fireCount)
					{
						Vec3d vecTracking = tracking.getPositionVector();
						Vec3d vecArrow = vecTracking.subtract(getPositionVector()).scale(0.01d);

						EntityFireball bullet = new EntityLargeFireball(worldObj, this, 0,0,0);
						bullet.accelerationX = vecArrow.xCoord;
						bullet.accelerationY = vecArrow.yCoord;
						bullet.accelerationZ = vecArrow.zCoord;

						if (! worldObj.isRemote) worldObj.spawnEntityInWorld(bullet);

						fireCount = 0;
					}
				} else
				{

					if (! worldObj.isRemote)
					{
						owner.addChatMessage(new TextComponentString(getName() + ": " + I18n.translateToLocalFormatted("entity." + EntityList.getEntityString(this) + ".chat.enemyNotFound", getName())));
					}
					setAttackTarget(null);
				}

			}

		}

		Vec3d motVec = new Vec3d(motionX, motionY, motionZ);
		if (getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() < motVec.lengthVector())
		{
			motVec = motVec.scale(1 /motVec.lengthVector()).scale(getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
		}

		motionX = motVec.xCoord;
		motionY = motVec.yCoord;
		motionZ = motVec.zCoord;

		super.onLivingUpdate();
		// TODO: enderEnergyの消費


	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand)
	{
		if (stack != null && stack.getItem() == CONItems.itemIntelligentStone && getStoneStack() == null)
		{
			ItemStack stoneStack = stack.copy();
			stoneStack.stackSize = 1;
			stack.stackSize --;
			setStoneStack(stoneStack);

			if (stack.stackSize <= 0) player.setHeldItem(hand, null);

			setOwnerId(player.getUniqueID());
			if (! worldObj.isRemote) player.addChatMessage(new TextComponentString(getName() + ": " + I18n.translateToLocalFormatted("entity." + EntityList.getEntityString(this) + ".chat.launch", getName())));

			return EnumActionResult.SUCCESS;

		} else if (getStoneStack() != null || player.isSneaking())
		{
			if (getStoneStack() != null) dropStoneStack(true);

			if (player.isSneaking())
			{
				if (! worldObj.isRemote) entityDropItem(new ItemStack(CONItems.itemFlyPod), 0.5f);
				setDead();
			}

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		if (getStoneStack() != null)
		{
			if (getOwner() instanceof EntityPlayerMP) getOwner().addChatMessage(new TextComponentString(getName() + ": " + I18n.translateToLocalFormatted("entity." + EntityList.getEntityString(this) + ".chat.dead", getName())));

			dropStoneStack(false);
		}

		super.onDeath(cause);
	}

	public ItemStack getStoneStack()
	{
		return dataManager.get(STONE).orNull();
	}

	public void setStoneStack(ItemStack stack)
	{
		dataManager.set(STONE, stack != null ? Optional.of(stack) : Optional.absent());
	}

	public void dropStoneStack(boolean removeStack)
	{
		if (! worldObj.isRemote) entityDropItem(getStoneStack(), 0.2f);

		if (removeStack)
		{
			setOwnerId(null);
			setStoneStack(null);
		}
	}

	@Override
	public String getName()
	{
		if (getStoneStack() != null) return getStoneStack().getDisplayName();
		return super.getName();
	}


	@Override
	protected void initEntityAI()
	{
		/*
		// tasks.addTask(1, new EntityAISwimming(this));
		// tasks.addTask(2, aiSit = new EntityAISit(this));
		// tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
		tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		// tasks.addTask(8, new EntityAIBeg(this, 8.0F));
		// tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		// tasks.addTask(9, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
		targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>()
		{
			@Override
			public boolean apply(Entity p_apply_1_)
			{
				return p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit;
			}
		}));
		targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntitySkeleton.class, false));
		*/
	}


	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}


	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		if (getOwnerId() != null) compound.setUniqueId("ownerUUID", getOwnerId());
		if (getStoneStack() != null) compound.setTag("stoneStack", getStoneStack().serializeNBT());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		if (compound.hasUniqueId("ownerUUID")) setOwnerId(compound.getUniqueId("ownerUUID"));
		if (compound.hasKey("stoneStack", NBT.TAG_COMPOUND)) setStoneStack(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("stoneStack")));
	}


	@Override
	public Team getTeam()
	{
		if (getOwner() != null)
		{
			return getOwner().getTeam();
		}

		return worldObj.getScoreboard().getPlayersTeam(getUniqueID().toString());
	}

	public void setOwnerId(UUID uuid)
	{
		dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(uuid));
	}

	/**
	* @inheritDoc
	*/
	@Override
	public UUID getOwnerId()
	{
		return dataManager.get(OWNER_UNIQUE_ID).orNull();
	}

	/**
	* @inheritDoc
	*/
	@Override
	public Entity getOwner()
	{
		UUID uuid = getOwnerId();
		return uuid == null ? null : worldObj.getPlayerEntityByUUID(uuid);
	}


}
