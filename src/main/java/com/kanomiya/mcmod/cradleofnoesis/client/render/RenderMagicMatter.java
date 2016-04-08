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
	private static final ResourceLocation stackedMaterialResource = new ResourceLocation(CradleOfNoesis.MODID + ":textures/entity/objStackedMaterial.png");

	protected ModelEnderPearl modelPearl;
	protected ModelStackedMaterial modelStackedMaterial;

	public RenderMagicMatter(RenderManager manager)
	{
		super(manager);

		modelPearl = new ModelEnderPearl();
		modelStackedMaterial = new ModelStackedMaterial();
	}

	@Override
	public void doRender(EntityMagicMatter entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		bindEntityTexture(entity);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		switch (entity.getForm())
		{
		case BLOCK:
			bindTexture(pearlResource);
			GlStateManager.translate(-0.5d, 0, -0.5d);
			GlStateManager.scale(0.5d, 0.5d, 0.5d);
			modelPearl.doRender(1f);
			break;
		case STACKED:
			bindTexture(stackedMaterialResource);
			GlStateManager.scale(0.1d, 0.1d, 0.1d);
			modelStackedMaterial.render(entity, 0,0,0,0,0, 1f); // TODO INVISIBLE ??
			break;
		}

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

