package com.kanomiya.mcmod.cradleofnoesis.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import com.google.common.base.Optional;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;


/**
 * @author Kanomiya
 *
 */
public class EntitySanctuary extends Entity
{
	protected static final DataParameter<Optional<ISanctuary>> SANCTUALY = EntityDataManager.createKey(EntitySanctuary.class, CradleOfNoesisAPI.SANCTUARY_DATASERIALIZER);

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
		ISanctuary sanctuary = getSanctuary();

		if (sanctuary != null)
		{
			sanctuary.onUpdate(worldObj, posX, posY, posZ);


			List<Entity> entityList = worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expandXyz(sanctuary.getRadius()));

			for (Entity entity: entityList)
			{
				if (entity.getDistanceToEntity(this) < sanctuary.getRadius())
				{
					sanctuary.onCollideWithEntity(worldObj, posX, posY, posZ, entity);
				}

			}
		} else
		{
			setDead();
		}


		super.onEntityUpdate();
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
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

	public void setSanctuary(ISanctuary sanctuary)
	{
		dataManager.set(SANCTUALY, Optional.fromNullable(sanctuary));
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return null;
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		return super.getEntityBoundingBox();
	}

	@Override
	protected void entityInit()
	{
		dataManager.register(SANCTUALY, Optional.<ISanctuary>absent());
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

		}

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		ISanctuary sanctuary = getSanctuary();
		if (sanctuary == null)
		{
			Class<? extends ISanctuary> clazz = CradleOfNoesisAPI.SANCUTUARY_REGISTRY.get(new ResourceLocation(compound.getString("sunctuaryId")));
			if (clazz != null)
			{
				try
				{
					sanctuary = clazz.newInstance();
					setSanctuary(sanctuary);
				} catch (InstantiationException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}

		if (sanctuary != null)
		{
			sanctuary.deserializeNBT(compound.getCompoundTag("sanctuary"));
		}

	}




}
