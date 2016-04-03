package com.kanomiya.mcmod.cradleofnoesis.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.inventory.ContainerTileEntityLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;


@SideOnly(Side.CLIENT)
public class GuiLiaAlter extends GuiContainer {
	private static final ResourceLocation background = new ResourceLocation(CradleOfNoesis.MODID + ":textures/gui/guiLiaAlter.png");
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");

	public GuiLiaAlter(InventoryPlayer inventoryPlayer, TileEntityLiaAlter tileEntity) {
		super(new ContainerTileEntityLiaAlter(inventoryPlayer, tileEntity));
		ySize = 183;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {

		fontRendererObj.drawString(StatCollector.translateToLocal("container.liaalter"), 8, 8, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(background);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

	}

}
