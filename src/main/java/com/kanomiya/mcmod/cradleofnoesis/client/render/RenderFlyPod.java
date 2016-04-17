package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityFlyPod;

/**
 * @author Kanomiya
 *
 */
public class RenderFlyPod extends RenderLiving<EntityFlyPod>
{
	protected static final ResourceLocation texture = new ResourceLocation(CradleOfNoesis.MODID + ":textures/entity/entityFlyPod.png");

	/**
	 * @param renderManager
	 */
	public RenderFlyPod(RenderManager renderManager)
	{
		super(renderManager, new ModelFlyPod(), 0.5f);
	}

	@Override
	public void doRender(EntityFlyPod entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		bindEntityTexture(entity);
		GlStateManager.pushMatrix();

		GlStateManager.disableLighting();
		GlStateManager.translate(x -0.5d, y, z -0.5d);
		GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);

		// RenderWolf
		mainModel.render(entity, 0f, 0f, 0f, entity.rotationYawHead, entity.rotationPitch, 1.0f);

		GlStateManager.popMatrix();

		// TODO 逆さ/ずれる...
		// super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	* @inheritDoc
	*/
	@Override
	protected ResourceLocation getEntityTexture(EntityFlyPod entity)
	{
		return texture;
	}

}
