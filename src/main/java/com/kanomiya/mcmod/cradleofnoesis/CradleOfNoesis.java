package com.kanomiya.mcmod.cradleofnoesis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.kanomiya.mcmod.cradleofnoesis.client.render.TESRLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.gui.GuiHandler;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEmeraldTablet;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;
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
		GameRegistry.register(CONBlocks.blockYuleOre);
		GameRegistry.register(new ItemBlock(CONBlocks.blockYuleOre).setRegistryName(CONBlocks.blockYuleOre.getRegistryName()));
		GameRegistry.register(CONBlocks.blockTsafaOre);
		GameRegistry.register(new ItemBlock(CONBlocks.blockTsafaOre).setRegistryName(CONBlocks.blockTsafaOre.getRegistryName()));

		GameRegistry.register(CONItems.itemIntelligentStone);
		GameRegistry.register(CONItems.itemYuleIngot);
		GameRegistry.register(CONItems.itemTsafaIngot);
		GameRegistry.register(CONItems.itemEmeraldTablet);

		GameRegistry.registerTileEntity(TileEntityLiaAlter.class, CradleOfNoesis.MODID + ":tileEntityLiaAlter");

		GameRegistry.addSmelting(CONBlocks.blockYuleOre, new ItemStack(CONItems.itemYuleIngot), 0.7f);
		GameRegistry.addSmelting(CONBlocks.blockTsafaOre, new ItemStack(CONItems.itemTsafaIngot), 0.7f);




		if (event.getSide().isClient())
		{
			Consumer<Item> simpleRegister = (item) -> ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + item.getRegistryName().getResourcePath()));
			BiConsumer<Item, Enum[]> enumRegister = new BiConsumer<Item, Enum[]>()
			{
				@Override
				public void accept(Item item, Enum[] enumAry)
				{
					String baseName = item.getRegistryName().getResourcePath();

					for (int i=0; i<enumAry.length; ++i)
					{
						ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(CradleOfNoesis.MODID + ":" + baseName + "_" + enumAry[i].name().toLowerCase()));
					}
				}
			};

			simpleRegister.accept(Item.getItemFromBlock(CONBlocks.blockLiaAlter));
			simpleRegister.accept(Item.getItemFromBlock(CONBlocks.blockYuleOre));
			simpleRegister.accept(Item.getItemFromBlock(CONBlocks.blockTsafaOre));

			enumRegister.accept(CONItems.itemIntelligentStone, ItemIntelligentStone.EnumType.values());
			enumRegister.accept(CONItems.itemEmeraldTablet, ItemEmeraldTablet.EnumType.values());

			simpleRegister.accept(CONItems.itemYuleIngot);
			simpleRegister.accept(CONItems.itemTsafaIngot);

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiaAlter.class, new TESRLiaAlter());

		}

		MinecraftForge.ORE_GEN_BUS.register(this);
	}


	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		PacketHandler.init();

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{

	}




	@SubscribeEvent
	public void onOreGeneratePre(OreGenEvent.Pre event)
	{
		WorldGenerator genYule = new WorldGenMinable(CONBlocks.blockYuleOre.getDefaultState(), 6);
		WorldGenerator genTsafa = new WorldGenMinable(CONBlocks.blockTsafaOre.getDefaultState(), 6);
		BlockPos pos = event.getPos();
		BiomeDecorator biomeDecorator = event.getWorld().getBiomeGenForCoords(pos).theBiomeDecorator;

		Method genStandardOre1 = null;

		try
		{
			// World, Random, int, WorldGenerator, int, int
			genStandardOre1 = ReflectionHelper.findMethod(
					BiomeDecorator.class, biomeDecorator,
					new String[] {"genStandardOre1", "func_76795_a"},
					World.class, Random.class, int.class, WorldGenerator.class, int.class, int.class);

		} catch (UnableToFindMethodException e)
		{
			e.printStackTrace();
		}

		if (genStandardOre1 != null)
		{
			try
			{
				if(TerrainGen.generateOre(event.getWorld(), event.getRand(), genYule, pos, OreGenEvent.GenerateMinable.EventType.CUSTOM))
					genStandardOre1.invoke(biomeDecorator, event.getWorld(), event.getRand(), 20, genYule, 0, 96);

				if(TerrainGen.generateOre(event.getWorld(), event.getRand(), genTsafa, pos, OreGenEvent.GenerateMinable.EventType.CUSTOM))
					genStandardOre1.invoke(biomeDecorator, event.getWorld(), event.getRand(), 20, genTsafa, 0, 96);

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}

	}



}
