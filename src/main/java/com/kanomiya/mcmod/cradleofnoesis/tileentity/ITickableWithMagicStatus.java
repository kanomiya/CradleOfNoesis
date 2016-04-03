package com.kanomiya.mcmod.cradleofnoesis.tileentity;

import net.minecraft.util.ITickable;

import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;

/**
 *
 * For TileEntity
 *
 * @author Kanomiya
 *
 */
public interface ITickableWithMagicStatus extends ITickable {

	MagicStatus getMagicStatus();

	@Override
	default void update()
	{
		MagicStatus magicStatus = getMagicStatus();
		if (magicStatus == null) return;

		magicStatus.update();
	}

}
