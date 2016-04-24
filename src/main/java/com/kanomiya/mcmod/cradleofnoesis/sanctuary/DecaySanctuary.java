package com.kanomiya.mcmod.cradleofnoesis.sanctuary;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.SimpleSanctuary;

/**
 * @author Kanomiya
 *
 */
public class DecaySanctuary extends SimpleSanctuary
{
	protected int interval, timer;
	protected int decayAmount;

	public DecaySanctuary()
	{
		this(0f,0,0,0);
	}

	public DecaySanctuary(float radius, int maxAge, int decayAmount, int interval)
	{
		super(radius, maxAge, 0xA03F8CAA);
		this.decayAmount = decayAmount;
		this.interval = interval;

		setUnlocalizedName("decaySanctuary");
	}


	@Override
	public void onUpdate(World worldIn, double posX, double posY, double posZ)
	{
		super.onUpdate(worldIn, posX, posY, posZ);

		++timer;
		if (interval < timer)
		{
			Vec3d vecPos = new Vec3d(posX, posY, posZ);

			for (int i=0; i<decayAmount; ++i)
			{
				Vec3d vecTgt = vecPos.addVector(worldIn.rand.nextGaussian() *radius*2 -radius, worldIn.rand.nextGaussian() *radius*2 -radius, worldIn.rand.nextGaussian() *radius -radius);
				if (vecPos.subtract(vecTgt).lengthVector() <= radius)
				{
					BlockPos blockPos = new BlockPos(vecTgt);

					if (worldIn.getTileEntity(blockPos) == null) // インスタントブロックを壊さないように...仕方ないね
					{
						worldIn.destroyBlock(blockPos, false);
					}

				}
			}


			timer = 0;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> tooltip, boolean advanced)
	{
		super.addInformation(tooltip, advanced);

		tooltip.add(I18n.translateToLocal("vocabulary.sanctuary.interval") + ": " + getInterval());
		tooltip.add(I18n.translateToLocal("vocabulary.sanctuary.decayAmount") + ": " + getDecayAmount());
	}

	public int getDecayAmount()
	{
		return decayAmount;
	}

	public void setDecayAmount(int decayAmount)
	{
		this.decayAmount = decayAmount;
	}

	/**
	 * @return interval
	 */
	public int getInterval()
	{
		return interval;
	}

	/**
	 * @param interval セットする interval
	 */
	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();

		nbt.setInteger("interval", interval);
		nbt.setInteger("timer", timer);
		nbt.setInteger("decayAmount", decayAmount);

		return nbt;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);

		interval = nbt.getInteger("interval");
		timer = nbt.getInteger("timer");
		decayAmount = nbt.getInteger("decayAmount");
	}



}
