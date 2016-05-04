package com.kanomiya.mcmod.cradleofnoesis.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.container.inventory.ContainerTileEntityLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;


@SideOnly(Side.CLIENT)
public class GuiLiaAlter extends GuiContainer {
	private static final ResourceLocation background = new ResourceLocation(CradleOfNoesisAPI.MODID, "textures/gui/guiLiaAlter.png");

	TileEntityLiaAlter tileAlter;

	public GuiLiaAlter(InventoryPlayer inventoryPlayer, TileEntityLiaAlter tileAlter) {
		super(new ContainerTileEntityLiaAlter(inventoryPlayer, tileAlter));
		ySize = 183;

		this.tileAlter = tileAlter;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {

		fontRendererObj.drawString(I18n.translateToLocal("container.liaalter"), 8, 8, 4210752);
		fontRendererObj.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(background);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		if (tileAlter.getStackInSlot(TileEntityLiaAlter.SLOT_FUEL1) != null)
		{
			int energyAmount = tileAlter.getField(2);
			int energyCapacity = tileAlter.getField(3);

			if (0 < energyCapacity)
			{
				int pixels = 18;
				int p = energyAmount*pixels /energyCapacity;

				this.drawTexturedModalRect(k +28, l +80, 176, 0, p, 2);
			}
		}

		if (tileAlter.getStackInSlot(TileEntityLiaAlter.SLOT_FUEL2) != null)
		{
			int energyAmount = tileAlter.getField(4);
			int energyCapacity = tileAlter.getField(5);

			if (0 < energyCapacity)
			{
				int pixels = 18;
				int p = energyAmount*pixels /energyCapacity;

				this.drawTexturedModalRect(k +64, l +80, 176, 0, p, 2);
			}
		}

		if (tileAlter.getStackInSlot(TileEntityLiaAlter.SLOT_OFFERING_KNOWLEDGE) != null)
		{
			int energyAmount = tileAlter.getField(6);
			int energyCapacity = tileAlter.getField(7);

			if (0 < energyCapacity)
			{
				int pixels = 18;
				int p = energyAmount*pixels /energyCapacity;

				this.drawTexturedModalRect(k +28, l +43, 176, 0, p, 2);
			}
		}

		if (tileAlter.getStackInSlot(TileEntityLiaAlter.SLOT_OFFERING2) != null)
		{
			int energyAmount = tileAlter.getField(8);
			int energyCapacity = tileAlter.getField(9);

			if (0 < energyCapacity)
			{
				int pixels = 18;
				int p = energyAmount*pixels /energyCapacity;

				this.drawTexturedModalRect(k +28, l +43, 176, 0, p, 2);
			}
		}

		if (tileAlter.isBrewing())
		{
			int brewingTime = tileAlter.getField(0);
			int brewingTimeInterval = tileAlter.getField(1);

			if (0 < brewingTimeInterval)
			{
				int pixels = 18;
				int p = brewingTime*pixels /brewingTimeInterval;

				this.drawTexturedModalRect(k +120, l +61, 176, 0, p, 2);
			}
		}


	}

}
