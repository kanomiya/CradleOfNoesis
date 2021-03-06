package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.google.common.base.Optional;
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

		Optional<ISanctuary> optSanctuary = entity.getSanctuary();
		if (! optSanctuary.isPresent()) return ;

		ISanctuary sanctuary = optSanctuary.get();

		GlStateManager.pushMatrix();
		GL11.glTranslated(x, y, z);

		float fx = entity.rotationYaw;
		float fy = entity.rotationPitch;
		GlStateManager.rotate(fx, 0, 0.5f, 0);
		GlStateManager.rotate(fy, -0.5f, 0, 0);

		sphere.setDrawStyle(GLU.GLU_FILL);
		sphere.setNormals(GLU.GLU_SMOOTH);

		int color = sanctuary.getColor();
		int a = color >> 24 & 0xFF;
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GlStateManager.enableBlend();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableAlpha();
		GlStateManager.disableLighting();
		GL11.glDepthMask(false);

		GL11.glColor4f(r/255f, g/255f, b/255f, a/255f);

		sphere.setOrientation(GLU.GLU_OUTSIDE);
		sphere.draw(sanctuary.getRadius(), 20, 20);
		sphere.setOrientation(GLU.GLU_INSIDE);
		sphere.draw(sanctuary.getRadius(), 20, 20); // TODO: Entityが描画順かなにかでAlpha適用されないことがある

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
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
