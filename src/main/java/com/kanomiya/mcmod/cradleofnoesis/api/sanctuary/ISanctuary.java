package com.kanomiya.mcmod.cradleofnoesis.api.sanctuary;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;


/**
 * @author Kanomiya
 *
 */
public interface ISanctuary extends INBTSerializable<NBTTagCompound>
{
	default void onUpdate(World worldIn, double posX, double posY, double posZ) {  }
	default void onCollideWithEntity(World worldIn, double posX, double posY, double posZ, Entity entity) {  }

	float getRadius();
	int getColor();

}
