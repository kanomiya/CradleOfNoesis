package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityFlyPod;

/**
 * @author Kanomiya
 *
 */
public class RenderFlyPod extends RenderLiving<EntityFlyPod>
{
	protected static final ResourceLocation texture = new ResourceLocation(CradleOfNoesisAPI.MODID, "textures/entity/entityFlyPod.png");

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


		// super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.pushMatrix();
		GL11.glTranslated(x, y, z);

		float fx = entity.rotationYaw;
		float fy = entity.rotationPitch;
		GlStateManager.rotate(fx, 0, 0.5f, 0);
		GlStateManager.rotate(fy, -0.5f, 0, 0);

		GL11.glTranslated(-0.5d, 0, -0.5d);
		GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
		GlStateManager.disableLighting();

		mainModel.render(entity, 0f, 0f, 0f, entity.rotationYawHead, entity.rotationPitch, 1.0f);

		GlStateManager.enableLighting();

		GlStateManager.popMatrix();

		/*
		GlStateManager.pushMatrix();

		GlStateManager.disableLighting();
		// GlStateManager.translate(x -0.5d, y, z -0.5d);

		GL11.glTranslated(x, y, z);
		GL11.glTranslated(-0.5d, 0, -0.5d); // TODO: Rotate in model origin

		GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
		GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) *partialTicks, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) *partialTicks, 0.0F, 0.0F, 1.0F);
		// RenderWolf
		mainModel.render(entity, 0f, 0f, 0f, entity.rotationYawHead, entity.rotationPitch, 1.0f);

		GlStateManager.popMatrix();
		// TODO 逆さ/ずれる...

		 */
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
