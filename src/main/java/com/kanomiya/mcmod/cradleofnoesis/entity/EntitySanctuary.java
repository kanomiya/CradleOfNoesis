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
	protected static final DataParameter<Optional<ISanctuary>> SANCTUALY = EntityDataManager.createKey(EntitySanctuary.class, CradleOfNoesisAPI.SANCTUARY_DATASERIALIZER);
	protected static final DataParameter<Optional<UUID>> SANCTUALY_UUID = EntityDataManager.createKey(EntitySanctuary.class, DataSerializers.OPTIONAL_UNIQUE_ID);


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
		setSize(sanctuary.getRadius(), sanctuary.getRadius());
	}


	@Override
	public void onEntityUpdate()
	{
		ISanctuary sanctuary = getSanctuary();

		if (sanctuary != null)
		{
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

							if (entity.noClip)
							{
								entity.motionY += 0.1d;
							}

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

	public ISanctuary getSanctuary()
	{
		return dataManager.get(SANCTUALY).orNull();
	}

	public UUID getSanctuaryUniqueID()
	{
		return dataManager.get(SANCTUALY_UUID).orNull();
	}

	public void setSanctuary(ISanctuary sanctuary)
	{
		dataManager.set(SANCTUALY, Optional.fromNullable(sanctuary));
		if (getSanctuaryUniqueID() == null) dataManager.set(SANCTUALY_UUID, Optional.of(getUniqueID()));
	}

	@Override
	protected void entityInit()
	{
		dataManager.register(SANCTUALY, Optional.<ISanctuary>absent());
		dataManager.register(SANCTUALY_UUID, Optional.<UUID>absent());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		ISanctuary sanctuary = getSanctuary();
		if (sanctuary != null)
		{
			ResourceLocation id = CradleOfNoesisAPI.SANCUTUARY_REGISTRY.inverse().get(sanctuary.getClass());
			if (id != null)
			{
				compound.setString("sanctuaryId", id.toString());
				compound.setTag("sanctuary", sanctuary.serializeNBT());
			}

			compound.setUniqueId("sanctuaryUUID", getSanctuaryUniqueID());
		}

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		ISanctuary sanctuary = getSanctuary();
		if (sanctuary == null)
		{
			sanctuary = CradleOfNoesisAPI.createSanctuaryInstance(new ResourceLocation(compound.getString("sanctuaryId")));
			dataManager.set(SANCTUALY, Optional.fromNullable(sanctuary));
		}

		if (sanctuary != null)
		{
			sanctuary.deserializeNBT(compound.getCompoundTag("sanctuary"));
			dataManager.set(SANCTUALY_UUID, Optional.of(compound.getUniqueId("sanctuaryUUID")));
		}

	}




}
