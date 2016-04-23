package com.kanomiya.mcmod.cradleofnoesis.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


/**
 * @author Kanomiya
 *
 */
public class EntitySpawnerBall<T extends Entity> extends EntityThrowable
{
	protected NBTTagCompound spawnEntityTag;

	public EntitySpawnerBall(World worldIn)
	{
		super(worldIn);
	}

	public EntitySpawnerBall(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntitySpawnerBall(World worldIn,  EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public void setSpawnEntity(Entity entity)
	{
		spawnEntityTag = entity.serializeNBT();
	}


	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);

		if (spawnEntityTag != null) compound.setTag("spawnEntityTag", spawnEntityTag);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		spawnEntityTag = compound.getCompoundTag("spawnEntityTag");
	}


	/**
	* @inheritDoc
	*/
	@Override
	protected void onImpact(RayTraceResult result)
	{
		if (spawnEntityTag == null)
		{
			setDead();
			return ;
		}

		Entity entity = EntityList.createEntityFromNBT(spawnEntityTag, worldObj);

		if (entity != null)
		{
			entity.setPosition(posX, posY, posZ);
			if (! worldObj.isRemote) worldObj.spawnEntityInWorld(entity);

			setDead();
		}

	}



}
