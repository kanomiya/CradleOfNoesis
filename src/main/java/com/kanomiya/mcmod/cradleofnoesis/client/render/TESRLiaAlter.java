package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;

/**
 *
 * LiaAlterのエンダーパール描画用
 *
 * @author Kanomiya
 *
 */
@SideOnly(Side.CLIENT)
public class TESRLiaAlter extends IExtendedTileEntitySpecialRenderer<TileEntityLiaAlter> {
	private static final ResourceLocation pearlResource = new ResourceLocation(CradleOfNoesisAPI.MODID, "textures/blocks/objEnderPearl.png");
	private ModelEnderPearl pearlmodel = new ModelEnderPearl();

	@Override public void renderTileEntityAt(TileEntityLiaAlter tile, double posX, double posY, double posZ, float rot_rot, int p_180535_9_)
	{
		GlStateManager.pushMatrix(); // 座標保存

		GlStateManager.translate((float)posX +0.5f, (float)posY -0.5f, (float)posZ +0.5f);
		GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
		GlStateManager.rotate(metaToRotate(tile.getBlockMetadata()) *90f, 0f, 1f, 0f);
		GlStateManager.enableCull(); // 背面描画の省略?

		bindTexture(pearlResource);

		if (tile.getStackInSlot(1) != null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(1f, 10f, -2f);
			pearlmodel.doRender(1f);
			GlStateManager.popMatrix();
		}

		if (tile.getStackInSlot(2) != null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(-3f, 10f, -2f);
			pearlmodel.doRender(1f);
			GlStateManager.popMatrix();
		}


		GlStateManager.popMatrix(); // 座標展開

	}

}
