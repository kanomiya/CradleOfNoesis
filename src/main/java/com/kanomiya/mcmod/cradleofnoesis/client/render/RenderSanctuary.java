package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;

/**
 * @author Kanomiya
 *
 */
public class RenderSanctuary extends Render<EntitySanctuary>
{
	protected static final ResourceLocation texture = new ResourceLocation(CradleOfNoesisAPI.MODID, "textures/entity/entityFlyPod.png");
	protected Sphere sphere = new Sphere();

	/**
	 * @param renderManager
	 */
	public RenderSanctuary(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntitySanctuary entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		bindEntityTexture(entity);

		ISanctuary sanctuary = entity.getSanctuary();
		if (sanctuary == null) return ;

		GlStateManager.pushMatrix();
		GL11.glTranslated(x, y, z);

		float fx = entity.rotationYaw;
		float fy = entity.rotationPitch;
		GlStateManager.rotate(fx, 0, 0.5f, 0);
		GlStateManager.rotate(fy, -0.5f, 0, 0);

		GL11.glTranslated(-0.5d, 0, -0.5d);

		sphere.setDrawStyle(GLU.GLU_LINE);
		int color = sanctuary.getColor();
		int r = color >> 24 & 0xFF;
		int g = color >> 16 & 0xFF;
		int b = color >> 8 & 0xFF;
		int a = color & 0xFF;

		GlStateManager.enableBlend();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.disableLighting();

		GL11.glColor4f(r/255f, g/255f, b/255f, a/255f);

		sphere.draw(sanctuary.getRadius(), 20, 20);

		GlStateManager.resetColor();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

	}

	/**
	* @inheritDoc
	*/
	@Override
	protected ResourceLocation getEntityTexture(EntitySanctuary entity)
	{
		return texture;
	}

}
