package com.kanomiya.mcmod.cradleofnoesis.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.kanomiya.mcmod.cradleofnoesis.CONGuis;
import com.kanomiya.mcmod.cradleofnoesis.inventory.ContainerTileEntityLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == CONGuis.GUIID_LIAALTER) {
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityLiaAlter) {
				return new ContainerTileEntityLiaAlter(player.inventory, (TileEntityLiaAlter) tileEntity);
			}
		}


		return null;
	}

	// returns an instance of the gui thingamabob
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == CONGuis.GUIID_LIAALTER) {
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityLiaAlter) {
				return new GuiLiaAlter(player.inventory, (TileEntityLiaAlter) tileEntity);
			}
		}

		return null;
	}

}
