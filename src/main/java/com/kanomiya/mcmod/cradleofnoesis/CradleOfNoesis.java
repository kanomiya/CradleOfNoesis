package com.kanomiya.mcmod.cradleofnoesis;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.kanomiya.mcmod.cradleofnoesis.client.render.TESRLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.gui.GuiHandler;
import com.kanomiya.mcmod.cradleofnoesis.network.PacketHandler;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;

/**
 * @author Kanomiya
 *
 */
@Mod(modid = CradleOfNoesis.MODID)
public class CradleOfNoesis {
	public static final String MODID = "cradleofnoesis";

	@Mod.Instance(MODID)
	public static CradleOfNoesis instance;

	public static final CreativeTabs tab = new CreativeTabs(MODID) {
		@Override @SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.dirt);
		}
	};

	public static Logger logger;


	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();

		GameRegistry.register(CONBlocks.blockLiaAlter);
		GameRegistry.register(new ItemBlock(CONBlocks.blockLiaAlter).setRegistryName(CONBlocks.blockLiaAlter.getRegistryName()));

		GameRegistry.register(CONItems.itemIntelligentStone);

		GameRegistry.registerTileEntity(TileEntityLiaAlter.class, CradleOfNoesis.MODID + ":tileEntityLiaAlter");

		if (event.getSide().isClient())
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CONBlocks.blockLiaAlter), 0, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + "blockLiaAlter"));
			ModelLoader.setCustomModelResourceLocation(CONItems.itemIntelligentStone, 0, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + "itemIntelligentStone_black", "inventory"));
			ModelLoader.setCustomModelResourceLocation(CONItems.itemIntelligentStone, 1, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + "itemIntelligentStone_white", "inventory"));
			ModelLoader.setCustomModelResourceLocation(CONItems.itemIntelligentStone, 2, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + "itemIntelligentStone_red", "inventory"));

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiaAlter.class, new TESRLiaAlter());

		}

		// MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		PacketHandler.init();

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{

	}


}
