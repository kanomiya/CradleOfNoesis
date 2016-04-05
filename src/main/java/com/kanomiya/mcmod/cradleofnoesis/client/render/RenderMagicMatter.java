package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityMagicMatter;

@SideOnly(Side.CLIENT)
public class RenderMagicMatter extends Render<EntityMagicMatter> {
	private static final ResourceLocation pearlResource = new ResourceLocation(CradleOfNoesis.MODID + ":textures/blocks/objEnderPearl.png");

	protected ModelEnderPearl modelPearl;

	public RenderMagicMatter(RenderManager manager) {
		super(manager);

		modelPearl = new ModelEnderPearl();
	}

	@Override
	public void doRender(EntityMagicMatter entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		bindEntityTexture(entity);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x -0.5d, y, z -0.5d);

		GlStateManager.scale(0.5d, 0.5d, 0.5d);
		modelPearl.doRender(1f);

		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	* @inheritDoc
	*/
	@Override
	protected ResourceLocation getEntityTexture(EntityMagicMatter entity)
	{
		return pearlResource;
	}



}

