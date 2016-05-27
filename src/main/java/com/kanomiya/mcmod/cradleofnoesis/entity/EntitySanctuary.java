package com.kanomiya.mcmod.cradleofnoesis.entity;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.google.common.base.Optional;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.event.SanctuaryPushEntityEvent;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;


/**
 * @author Kanomiya
 *
 */
public class EntitySanctuary extends Entity
{
	protected static final DataParameter<Optional<ISanctuary>> SANCTUARY = EntityDataManager.createKey(EntitySanctuary.class, CradleOfNoesisAPI.SANCTUARY_DATASERIALIZER);
	protected static final DataParameter<Optional<UUID>> SANCTUARY_UUID = EntityDataManager.createKey(EntitySanctuary.class, DataSerializers.OPTIONAL_UNIQUE_ID);


	/**
	 * @param worldIn
	 */
	public EntitySanctuary(World worldIn)
	{
		super(worldIn);

		ignoreFrustumCheck = true; // 視界外でも描画するよ
	}

	/**
	 *
	 * @param worldIn
	 * @param sanctuary
	 */
	public EntitySanctuary(World worldIn, ISanctuary sanctuary)
	{
		this(worldIn);

		setSanctuary(sanctuary);
	}


	@Override
	public void onEntityUpdate()
	{
		Optional<ISanctuary> optSanctuary = getSanctuary();

		if (optSanctuary.isPresent())
		{
			ISanctuary sanctuary = optSanctuary.get();

			sanctuary.onUpdate(worldObj, posX, posY, posZ);

			List<Entity> entityList = worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expandXyz(sanctuary.getRadius()));

			for (Entity entity: entityList)
			{
				float dist = entity.getDistanceToEntity(this);
				if (dist < sanctuary.getRadius())
				{
					sanctuary.onCollideWithEntity(worldObj, posX, posY, posZ, entity);
				}

				if (dist < sanctuary.getRadius() +0.5d)
				{
					if (! sanctuary.isAllowedToEnter(entity))
					{
						if (! MinecraftForge.EVENT_BUS.post(new SanctuaryPushEntityEvent(sanctuary, entity)))
						{
							Vec3d vec = entity.getPositionVector().subtract(getPositionVector());
							vec = vec.scale(1 /vec.lengthVector()).scale(0.5d);
							entity.motionX = vec.xCoord;
							entity.motionY = vec.yCoord;
							entity.motionZ = vec.zCoord;

							if (entity.noClip) entity.motionY = 0.1d;

							entity.attackEntityFrom(DamageSource.inWall, 1.0f);

						}

					}
				}

			}

			if (sanctuary.getMaxAge() < sanctuary.getAge()) setDead();

		} else
		{
			setDead();
		}


		super.onEntityUpdate();
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return ! isDead;
	}

	@Override
	public boolean isPushedByWater()
	{
		return false;
	}

	public Optional<ISanctuary> getSanctuary()
	{
		return dataManager.get(SANCTUARY);
	}

	public UUID getSanctuaryUniqueID()
	{
		return dataManager.get(SANCTUARY_UUID).orNull();
	}

	public void setSanctuary(ISanctuary sanctuary)
	{
		dataManager.set(SANCTUARY, Optional.fromNullable(sanctuary));
		if (getSanctuaryUniqueID() == null) dataManager.set(SANCTUARY_UUID, Optional.of(getUniqueID())); // 初回登録時のUUIDを記録
	}

	@Override
	protected void entityInit()
	{
		dataManager.register(SANCTUARY, Optional.<ISanctuary>absent());
		dataManager.register(SANCTUARY_UUID, Optional.<UUID>absent());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		Optional<ISanctuary> optSanctuary = getSanctuary();
		if (optSanctuary.isPresent())
		{
			Optional<ResourceLocation> optId = CradleOfNoesisAPI.getSanctuaryId(optSanctuary.get().getClass());
			if (optId.isPresent())
			{
				compound.setString("sanctuaryId", optId.get().toString());
				compound.setTag("sanctuary", optSanctuary.get().serializeNBT());
			}

			compound.setUniqueId("sanctuaryUUID", getSanctuaryUniqueID());
		}

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		Optional<ISanctuary> optSanctuary = getSanctuary();
		if (! optSanctuary.isPresent())
		{
			optSanctuary = CradleOfNoesisAPI.createSanctuaryInstance(new ResourceLocation(compound.getString("sanctuaryId")));
			dataManager.set(SANCTUARY, optSanctuary);
		}

		if (optSanctuary.isPresent())
		{
			optSanctuary.get().deserializeNBT(compound.getCompoundTag("sanctuary"));
			dataManager.set(SANCTUARY_UUID, Optional.of(compound.getUniqueId("sanctuaryUUID")));
		}

	}




}
