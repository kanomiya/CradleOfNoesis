package com.kanomiya.mcmod.cradleofnoesis;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.kanomiya.mcmod.cradleofnoesis.block.BlockLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.block.BlockMagicBattery;
import com.kanomiya.mcmod.cradleofnoesis.client.gui.GuiIngameHandler;
import com.kanomiya.mcmod.cradleofnoesis.client.render.TESRLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.command.CommandMagicStatus;
import com.kanomiya.mcmod.cradleofnoesis.event.AttachCapabilitiesEventHandler;
import com.kanomiya.mcmod.cradleofnoesis.event.PlayerInteractionEventHandler;
import com.kanomiya.mcmod.cradleofnoesis.event.UpdateEventHandler;
import com.kanomiya.mcmod.cradleofnoesis.gui.GuiHandler;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;
import com.kanomiya.mcmod.cradleofnoesis.network.PacketHandler;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityMagicBattery;

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


	public static class CONGuis
	{
		public static int GUIID_LIAALTER = 0;
	}

	public static class CONBlocks
	{
		public static BlockMagicBattery blockMagicBattery = new BlockMagicBattery();
		public static BlockLiaAlter blockLiaAlter = new BlockLiaAlter();
	}

	public static class CONItems
	{
		public static ItemIntelligentStone itemIntelligentStone = new ItemIntelligentStone();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();

		GameRegistry.registerBlock(CONBlocks.blockMagicBattery);
		GameRegistry.registerBlock(CONBlocks.blockLiaAlter);

		GameRegistry.registerItem(CONItems.itemIntelligentStone);

		GameRegistry.registerTileEntity(TileEntityMagicBattery.class, "tileEntityMagicBattery");
		GameRegistry.registerTileEntity(TileEntityLiaAlter.class, "tileEntityLiaAlter");

		MinecraftForge.EVENT_BUS.register(AttachCapabilitiesEventHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(PlayerInteractionEventHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(UpdateEventHandler.INSTANCE);

		if (event.getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(new GuiIngameHandler());

			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CONBlocks.blockLiaAlter), 0, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + "blockLiaAlter"));
			ModelLoader.setCustomModelResourceLocation(CONItems.itemIntelligentStone, 0, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + "itemIntelligentStone_black", "inventory"));
			ModelLoader.setCustomModelResourceLocation(CONItems.itemIntelligentStone, 1, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + "itemIntelligentStone_white", "inventory"));
			ModelLoader.setCustomModelResourceLocation(CONItems.itemIntelligentStone, 2, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + "itemIntelligentStone_red", "inventory"));

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiaAlter.class, new TESRLiaAlter());

		}

		CapabilityManager.INSTANCE.register(MagicStatus.class, new MagicStatus.Storage(), MagicStatus::createDefault);

		// MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		PacketHandler.init();

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandMagicStatus());

	}


}
