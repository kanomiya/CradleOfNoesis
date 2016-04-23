package com.kanomiya.mcmod.cradleofnoesis.api.sanctuary;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;


/**
 *
 * 聖域 - Sanctuary
 *
 * 必ず無引数コンストラクタ作ってね
 * You MUST create a constructor with no argument
 *
 * @author Kanomiya
 *
 */
public interface ISanctuary extends INBTSerializable<NBTTagCompound>
{
	default void onUpdate(World worldIn, double posX, double posY, double posZ) {  }
	default void onCollideWithEntity(World worldIn, double posX, double posY, double posZ, Entity entity) {  }

	float getRadius();
	int getColor();
	int getAge();
	int getMaxAge();

	void allowToEnter(Entity entity);
	void disallowToEnter(Entity entity);
	boolean isAllowedToEnter(Entity entity);

}
