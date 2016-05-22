package com.kanomiya.mcmod.cradleofnoesis.magic.hook.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.kanomiya.mcmod.cradleofnoesis.magic.hook.world.WorldSavedDataMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public enum MagicRenderTickEventHandler
{
	INSTANCE;

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event)
	{
		if (event.phase != Phase.END) return ;

		Minecraft mc = Minecraft.getMinecraft();
		World worldIn = mc.theWorld;
		if (worldIn == null) return;

		EntityPlayer playerIn = mc.thePlayer;
		WorldSavedDataMagic worldMagicData = WorldSavedDataMagic.get(worldIn);

		mc.mcProfiler.endStartSection("magic");

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		GL11.glDepthMask(false);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);


		GL11.glColor3f(1.0f, 1.0f, 0f);

		/*
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();

		if (renderer != null)
		{
			renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

			double minX = playerIn.posX;
			double minY = playerIn.posY;
			double minZ = playerIn.posZ;
			double maxX = playerIn.posX +1.0d;
			double maxY = playerIn.posY +1.0d;
			double maxZ = playerIn.posZ +1.0d;

			renderer.pos(minX, minY, minZ).endVertex();
			renderer.pos(minX, minY, maxZ).endVertex();
			renderer.pos(maxX, minY, maxZ).endVertex();
			renderer.pos(maxX, minY, minZ).endVertex();

			renderer.pos(minX, maxY, maxZ).endVertex();
			renderer.pos(maxX, maxY, maxZ).endVertex();
			renderer.pos(maxX, maxY, minZ).endVertex();
			renderer.pos(minX, maxY, maxZ).endVertex();


			tessellator.draw();
		}
		 */

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glPushMatrix();
		GL11.glTranslatef(0, 100, 0);
		Sphere sphere = new Sphere();
		sphere.setDrawStyle(GLU.GLU_LINE);
		sphere.draw(50, 10, 10);
		GL11.glPopMatrix();

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_CULL_FACE);

		mc.mcProfiler.endSection();


	}

}
