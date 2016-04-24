package com.kanomiya.mcmod.cradleofnoesis.api.sanctuary;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


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
	void setRadius(float radius);

	int getColor();
	void getColor(int color);

	int getAge();
	void setAge(int age);

	int getMaxAge();
	void setMaxAge(int maxAge);

	String getUnlocalizedName();
	void setUnlocalizedName(String unlocalizedName);

	default String getLocalizedName()
	{
		return I18n.translateToLocal("sanctuary." + getUnlocalizedName() + ".name");
	}

	@SideOnly(Side.CLIENT)
	default void addInformation(List<String> tooltip, boolean advanced) {  }

	void allowToEnter(Entity entity);
	void disallowToEnter(Entity entity);
	boolean isAllowedToEnter(Entity entity);

}
