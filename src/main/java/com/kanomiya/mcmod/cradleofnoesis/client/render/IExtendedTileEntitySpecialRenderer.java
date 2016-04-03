package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 *
 * 横回転比取得メソッド付きTileEntitySpecialRenderer
 *
 * @author Kanomiya
 *
 */
public abstract class IExtendedTileEntitySpecialRenderer<T extends TileEntity> extends TileEntitySpecialRenderer<T> {

	/**
	 * 横回転比の取得
	 *
	 * @param meta
	 * @return 回転比
	 */
	public float metaToRotate(int meta) {
		switch (meta) {
		case 0: return 2;
		case 1: return 1;
		case 2: return 0;
		case 3: return 3;
		default: return 0;

		}
	}
}
