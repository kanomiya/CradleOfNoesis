package com.kanomiya.mcmod.cradleofnoesis.client.gui;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;

// GuiIngameForge

/**
 * @author Kanomiya
 *
 */
@SideOnly(Side.CLIENT)
public class GuiIngameHandler extends Gui {
	public static final ResourceLocation icons = new ResourceLocation(CradleOfNoesis.MODID + ":textures/gui/icons.png");

	public static boolean drawMagicalInfo = false;
	public static int localHeight = GuiIngameForge.left_height;

	public static Minecraft minecraft = FMLClientHandler.instance().getClient();
	protected final Random rand = new Random();

	@SubscribeEvent @SideOnly(Side.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {

		if (event.type != RenderGameOverlayEvent.ElementType.HELMET) return;

		int width = event.resolution.getScaledWidth();
		int height = event.resolution.getScaledHeight();

		if (minecraft.playerController.shouldDrawHUD() && minecraft.thePlayer.hasCapability(CradleOfNoesisAPI.capMagicStatus, null))
		{
			MagicStatus magicStatus = minecraft.thePlayer.getCapability(CradleOfNoesisAPI.capMagicStatus, null);

			if (0 <= magicStatus.getMp() && 0 < magicStatus.getMpCapacity())
			{
				renderMagicPoint(magicStatus, width, height);

				GuiIngameForge.left_height += 12;
				GuiIngameForge.right_height += 12;

				bind(Gui.icons);
			}
		}


	}

	@SideOnly(Side.CLIENT)
	protected void renderMagicPoint(MagicStatus magicStatus, int width, int height) {
		minecraft.mcProfiler.startSection(CradleOfNoesis.MODID + ".energyMagic");

		GlStateManager.enableBlend();
		bind(icons);

		int left = width / 2 -91;
		int top = height -localHeight;

		int mp = magicStatus.getMp();
		int mpCap = magicStatus.getMpCapacity();

		drawTexturedModalRect(left, top, 0, 0, 182, 5);

		int w = mp*182 /mpCap +1;
		drawTexturedModalRect(left, top, 0, 5, w, 5);

		/*
		if (0 < ms.getFloatingMP()) {
			int fw = ms.getFloatingMP() *182 /maxmp +1;
			drawTexturedModalRect(left +w -1, top, w -1, 10, fw, 5);
		}
		*/

		String text = mp + "/" + mpCap;

		/*
		ItemStack heldStack = minecraft.thePlayer.getHeldItem();

		if (heldStack != null && heldStack.getItem() instanceof IMagicItem) {
			NBTTagCompound magicNbt = MagicNBTUtils.getMagicNBT(heldStack);

			int itemMp = KMagicAPI.getMp(magicNbt);
			int itemMaxMp = KMagicAPI.getMaxMp(magicNbt);

			if (0 < itemMaxMp) { text += " (" + itemMp + "/" + itemMaxMp +")"; }

		}
		*/

		int color = 8453920;
		int x = (width - minecraft.fontRendererObj.getStringWidth(text)) / 2;
		int y = top - 4;
		minecraft.fontRendererObj.drawString(text, x + 1, y, 0);
		minecraft.fontRendererObj.drawString(text, x - 1, y, 0);
		minecraft.fontRendererObj.drawString(text, x, y + 1, 0);
		minecraft.fontRendererObj.drawString(text, x, y - 1, 0);
		minecraft.fontRendererObj.drawString(text, x, y, color);

		GlStateManager.disableBlend();
		minecraft.mcProfiler.endSection();
	}






	private void bind(ResourceLocation res) {
		minecraft.getTextureManager().bindTexture(res);
	}

	/*
	public void drawTexturedModalRect(int x, int y, int iconX, int iconY, int iconW, int iconH)
	{
		float zLevel = -90.0F;

		float f = 0.0039063F;
		float f1 = 0.0039063F;

		Tessellator tessellator = Tessellator.getInstance();

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + iconH, zLevel, (iconX + 0) * f, (iconY + iconH) * f1);
		tessellator.addVertexWithUV(x + iconW, y + iconH, zLevel, (iconX + iconW) * f, (iconY + iconH) * f1);
		tessellator.addVertexWithUV(x + iconW, y + 0, zLevel, (iconX + iconW) * f, (iconY + 0) * f1);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, (iconX + 0) * f, (iconY + 0) * f1);


		tessellator.draw();

	}
	 */





}
