package com.kanomiya.mcmod.cradleofnoesis;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.kanomiya.mcmod.cradleofnoesis.block.BlockMagicBattery;
import com.kanomiya.mcmod.cradleofnoesis.client.gui.GuiIngameHandler;
import com.kanomiya.mcmod.cradleofnoesis.event.EventHandlerPlayerInteraction;
import com.kanomiya.mcmod.cradleofnoesis.event.EventHandlerUpdate;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityMagicBattery;
import com.kanomiya.mcmod.energyway.api.EnergyOwnerInitRegistry;
import com.kanomiya.mcmod.energyway.api.EnergyWayAPI;
import com.kanomiya.mcmod.energyway.api.energy.Energy;
import com.kanomiya.mcmod.energyway.api.energy.EnergyType;

/**
 * @author Kanomiya
 *
 */
@Mod(modid = CradleOfNoesis.MODID)
public class CradleOfNoesis {
	public static final String MODID = "cradleofnoesis";

	@Mod.Instance(MODID)
	public static CradleOfNoesis instance;

	public static final EnergyType energyTypeMagic = new EnergyType("magic");

	public static final CreativeTabs tab = new CreativeTabs(MODID) {
		@Override @SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.dirt);
		}
	};

	public static Logger logger;



	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		EnergyWayAPI.registerEnergyType(energyTypeMagic);

		GameRegistry.registerBlock(new BlockMagicBattery());
		GameRegistry.registerTileEntity(TileEntityMagicBattery.class, "tileEntityMagicBattery");

		MinecraftForge.EVENT_BUS.register(EventHandlerPlayerInteraction.INSTANCE);
		MinecraftForge.EVENT_BUS.register(EventHandlerUpdate.INSTANCE);

		if (event.getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(new GuiIngameHandler());
		}

		Energy e = Energy.create(energyTypeMagic, 100, 100);

		EnergyOwnerInitRegistry.INSTANCE.addEntityPropTemplete(EntityPlayer.class, e.copy());
		// MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		// NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}


}
