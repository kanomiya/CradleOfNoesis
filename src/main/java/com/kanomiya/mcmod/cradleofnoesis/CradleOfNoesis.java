package com.kanomiya.mcmod.cradleofnoesis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.ListItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import com.kanomiya.mcmod.cradleofnoesis.client.render.RenderFlyPod;
import com.kanomiya.mcmod.cradleofnoesis.client.render.TESRLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityFlyPod;
import com.kanomiya.mcmod.cradleofnoesis.gui.GuiHandler;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEmeraldTablet;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;
import com.kanomiya.mcmod.cradleofnoesis.network.PacketHandler;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.villager.SimpleVillagerCareer;
import com.kanomiya.mcmod.cradleofnoesis.villager.SimpleVillagerProfession;

/**
 * @author Kanomiya
 *
 */
@Mod(modid = CradleOfNoesis.MODID)
public class CradleOfNoesis {
	public static final String MODID = "com.kanomiya.mcmod.cradleofnoesis";

	@Mod.Instance(MODID)
	public static CradleOfNoesis instance;

	public static final CreativeTabs tab = new CreativeTabs(MODID) {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return CONItems.itemIntelligentStone;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public int getIconItemDamage()
		{
			return ItemIntelligentStone.EnumType.RED.ordinal();
		}
	};

	public static Logger logger;
	public static SimpleVillagerProfession vprofArchaeologist;

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
		GameRegistry.register(CONItems.itemFlyPod);


		GameRegistry.registerTileEntity(TileEntityLiaAlter.class, CradleOfNoesis.MODID + ":tileEntityLiaAlter");

		GameRegistry.addSmelting(CONBlocks.blockYuleOre, new ItemStack(CONItems.itemYuleIngot), 0.7f);
		GameRegistry.addSmelting(CONBlocks.blockTsafaOre, new ItemStack(CONItems.itemTsafaIngot), 0.7f);

		EntityRegistry.registerModEntity(EntityFlyPod.class, "entityFlyPod", 0, instance, 32, 1, true);


		vprofArchaeologist = new SimpleVillagerProfession(
				new ResourceLocation(CradleOfNoesis.MODID, "archaeologist"),
				new ResourceLocation(CradleOfNoesis.MODID + ":textures/entity/villager/archaeologist.png"));

		new SimpleVillagerCareer(vprofArchaeologist, "emeraldTablet").init(
			new ITradeList[][]
			{
				{
					new ListItemForEmeralds(new ItemStack(Items.ENDER_PEARL, 1), new PriceInfo(4, 7)),
				},
				{
					new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.LIA_FALIA.ordinal()), new PriceInfo(12, 14)),

					new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.LIA_STILIA.ordinal()), new PriceInfo(12, 14)),

					new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.LIA_REGILIA.ordinal()), new PriceInfo(12, 14)),

					new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.ARLEY.ordinal()), new PriceInfo(16, 22)),

					new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.ARMES.ordinal()), new PriceInfo(26, 32)),
				},
			}
		);

		GameRegistry.register(vprofArchaeologist);



		if (event.getSide().isClient())
		{
			Consumer<Item> simpleRegister = (item) -> ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			BiConsumer<Item, Enum[]> enumRegister = new BiConsumer<Item, Enum[]>()
			{
				@Override
				public void accept(Item item, Enum[] enumAry)
				{
					for (int i=0; i<enumAry.length; ++i)
					{
						ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(item.getRegistryName().getResourceDomain(), item.getRegistryName().getResourcePath() + "_" + enumAry[i].name().toLowerCase()), "inventory"));
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
			simpleRegister.accept(CONItems.itemFlyPod);

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiaAlter.class, new TESRLiaAlter());

			RenderingRegistry.registerEntityRenderingHandler(EntityFlyPod.class, RenderFlyPod::new);
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
