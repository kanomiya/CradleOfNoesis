package com.kanomiya.mcmod.cradleofnoesis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.logging.log4j.Logger;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.event.SanctuaryPushEntityEvent;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuaryInfo;
import com.kanomiya.mcmod.cradleofnoesis.client.gui.GuiHandler;
import com.kanomiya.mcmod.cradleofnoesis.client.render.RenderFlyPod;
import com.kanomiya.mcmod.cradleofnoesis.client.render.RenderSanctuary;
import com.kanomiya.mcmod.cradleofnoesis.client.render.TESRLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityFlyPod;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySpawnerBall;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemBlockInstantSanctuary;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEmeraldTablet;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemEntitySpawner;
import com.kanomiya.mcmod.cradleofnoesis.item.ItemIntelligentStone;
import com.kanomiya.mcmod.cradleofnoesis.magic.hook.MagicWorldTickEventHandler;
import com.kanomiya.mcmod.cradleofnoesis.magic.hook.client.render.MagicRenderTickEventHandler;
import com.kanomiya.mcmod.cradleofnoesis.network.PacketHandler;
import com.kanomiya.mcmod.cradleofnoesis.network.datasync.DataSerializerNBT;
import com.kanomiya.mcmod.cradleofnoesis.sanctuary.DecaySanctuary;
import com.kanomiya.mcmod.cradleofnoesis.sanctuary.HealSanctuary;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntitySanctuary;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityVillager.ListItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataSerializers;
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
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Kanomiya
 *
 */
@Mod(modid = CradleOfNoesisAPI.MODID)
public class CradleOfNoesis
{
	@Mod.Instance(CradleOfNoesisAPI.MODID)
	public static CradleOfNoesis instance;

	public static final CreativeTabs tab = new CreativeTabs(CradleOfNoesisAPI.MODID) {
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
	public static VillagerProfession vprofArchaeologist;

	public static final DataSerializerNBT DATASERIALIZER_NBT = new DataSerializerNBT();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();

		CradleOfNoesisAPI.registerSanctuary(new ResourceLocation(CradleOfNoesisAPI.MODID, "heal"), HealSanctuary.class, new ISanctuaryInfo()
		{
			@Override
			public ISanctuary createForInstantBlock()
			{
				return new HealSanctuary(5.0f, Short.MAX_VALUE, 0.1f, 5);
			}

			@Override
			public ISanctuary createForInstantItem()
			{
				return new HealSanctuary(5.0f, 500, 0.1f, 5);
			}
		});

		CradleOfNoesisAPI.registerSanctuary(new ResourceLocation(CradleOfNoesisAPI.MODID, "decay"), DecaySanctuary.class, new ISanctuaryInfo()
		{
			@Override
			public ISanctuary createForInstantBlock()
			{
				return new DecaySanctuary(5.0f, Short.MAX_VALUE, 100, 5);
			}

			@Override
			public ISanctuary createForInstantItem()
			{
				return new DecaySanctuary(5.0f, 500, 100, 5);
			}
		});

		GameRegistry.register(CONBlocks.blockLiaAlter);
		GameRegistry.register(new ItemBlock(CONBlocks.blockLiaAlter).setRegistryName(CONBlocks.blockLiaAlter.getRegistryName()));
		GameRegistry.register(CONBlocks.blockYuleOre);
		GameRegistry.register(new ItemBlock(CONBlocks.blockYuleOre).setRegistryName(CONBlocks.blockYuleOre.getRegistryName()));
		GameRegistry.register(CONBlocks.blockTsafaOre);
		GameRegistry.register(new ItemBlock(CONBlocks.blockTsafaOre).setRegistryName(CONBlocks.blockTsafaOre.getRegistryName()));
		GameRegistry.register(CONBlocks.blockRanimOre);
		GameRegistry.register(new ItemBlock(CONBlocks.blockRanimOre).setRegistryName(CONBlocks.blockRanimOre.getRegistryName()));
		GameRegistry.register(CONBlocks.blockSanctuary);
		GameRegistry.register(new ItemBlock(CONBlocks.blockSanctuary).setRegistryName(CONBlocks.blockSanctuary.getRegistryName()));
		GameRegistry.register(CONBlocks.blockInstantSanctuary);
		GameRegistry.register(new ItemBlockInstantSanctuary(CONBlocks.blockInstantSanctuary).setRegistryName(CONBlocks.blockInstantSanctuary.getRegistryName()));

		GameRegistry.register(CONItems.itemIntelligentStone);
		GameRegistry.register(CONItems.itemYuleIngot);
		GameRegistry.register(CONItems.itemTsafaIngot);
		GameRegistry.register(CONItems.itemRanimIngot);
		GameRegistry.register(CONItems.itemEmeraldTablet);
		GameRegistry.register(CONItems.itemFlyPod);
		GameRegistry.register(CONItems.itemInstantSanctuary);
		GameRegistry.register(CONItems.itemSanctuaryRemover);


		GameRegistry.registerTileEntity(TileEntityLiaAlter.class, CradleOfNoesisAPI.MODID + ":tileEntityLiaAlter");
		GameRegistry.registerTileEntity(TileEntitySanctuary.class, CradleOfNoesisAPI.MODID + ":tileEntitySanctuary");

		GameRegistry.addSmelting(CONBlocks.blockYuleOre, new ItemStack(CONItems.itemYuleIngot), 0.7f);
		GameRegistry.addSmelting(CONBlocks.blockTsafaOre, new ItemStack(CONItems.itemTsafaIngot), 0.7f);
		GameRegistry.addSmelting(CONBlocks.blockRanimOre, new ItemStack(CONItems.itemRanimIngot), 0.7f);


		GameRegistry.addRecipe(new ItemStack(CONBlocks.blockLiaAlter), //TODO: デバッグ用仮レシピ
				"TTT",
				"TMT",
				"TTT",
				'T', CONItems.itemTsafaIngot,
				'M', Items.ENDER_EYE
			);

		GameRegistry.addRecipe(new ItemStack(CONItems.itemSanctuaryRemover), //TODO: デバッグ用仮レシピ
				"RRR",
				" R ",
				"RRR",
				'R', CONItems.itemRanimIngot
			);

		ItemStack stack;
		stack = new ItemStack(CONItems.itemInstantSanctuary);
		stack.setTagInfo(CradleOfNoesisAPI.DATAID_SANCTUARYSET,
				CradleOfNoesisAPI.serializeSanctuary(CradleOfNoesisAPI.getSanctuaryInfo(HealSanctuary.class).get().createForInstantItem()).get());
		GameRegistry.addRecipe(stack, //TODO: デバッグ用仮レシピ
				"YY",
				'Y', CONItems.itemYuleIngot
			);

		stack = new ItemStack(CONItems.itemInstantSanctuary, 1, 1);
		stack.setTagInfo(CradleOfNoesisAPI.DATAID_SANCTUARYSET,
				CradleOfNoesisAPI.serializeSanctuary(CradleOfNoesisAPI.getSanctuaryInfo(HealSanctuary.class).get().createForInstantItem()).get());
		GameRegistry.addRecipe(stack, //TODO: デバッグ用仮レシピ
				"Y",
				'Y', CONItems.itemYuleIngot
			);

		stack = new ItemStack(CONItems.itemInstantSanctuary);
		stack.setTagInfo(CradleOfNoesisAPI.DATAID_SANCTUARYSET,
				CradleOfNoesisAPI.serializeSanctuary(CradleOfNoesisAPI.getSanctuaryInfo(DecaySanctuary.class).get().createForInstantItem()).get());
		GameRegistry.addRecipe(stack, //TODO: デバッグ用仮レシピ
				"TT",
				'T', CONItems.itemTsafaIngot
			);

		stack = new ItemStack(CONItems.itemInstantSanctuary, 1, 1);
		stack.setTagInfo(CradleOfNoesisAPI.DATAID_SANCTUARYSET,
				CradleOfNoesisAPI.serializeSanctuary(CradleOfNoesisAPI.getSanctuaryInfo(DecaySanctuary.class).get().createForInstantItem()).get());
		GameRegistry.addRecipe(stack, //TODO: デバッグ用仮レシピ
				"T",
				'T', CONItems.itemTsafaIngot
			);

		stack = new ItemStack(CONBlocks.blockInstantSanctuary);
		stack.setTagInfo(CradleOfNoesisAPI.DATAID_SANCTUARYSET,
				CradleOfNoesisAPI.serializeSanctuary(CradleOfNoesisAPI.getSanctuaryInfo(HealSanctuary.class).get().createForInstantItem()).get());
		GameRegistry.addRecipe(stack, //TODO: デバッグ用仮レシピ
				"YY",
				"YY",
				'Y', CONItems.itemYuleIngot
			);

		stack = new ItemStack(CONBlocks.blockInstantSanctuary);
		stack.setTagInfo(CradleOfNoesisAPI.DATAID_SANCTUARYSET,
				CradleOfNoesisAPI.serializeSanctuary(CradleOfNoesisAPI.getSanctuaryInfo(DecaySanctuary.class).get().createForInstantItem()).get());
		GameRegistry.addRecipe(stack, //TODO: デバッグ用仮レシピ
				"TT",
				"TT",
				'T', CONItems.itemTsafaIngot
			);



		int etId = -1; // EntityList
		EntityRegistry.registerModEntity(EntityFlyPod.class, "entityFlyPod", ++etId, instance, 32, 1, true);
		EntityRegistry.registerModEntity(EntitySanctuary.class, "entitySanctuary", ++etId, instance, 128, 1, true);
		EntityRegistry.registerModEntity(EntitySpawnerBall.class, "entitySpawnerBall", ++etId, instance, 32, 1, true);


		vprofArchaeologist = new VillagerProfession(
				new ResourceLocation(CradleOfNoesisAPI.MODID, "archaeologist").toString(),
				new ResourceLocation(CradleOfNoesisAPI.MODID + ":textures/entity/villager/archaeologist.png").toString());

		new VillagerCareer(vprofArchaeologist, "emeraldTablet")
		.addTrade(0,
				new ListItemForEmeralds(new ItemStack(Items.ENDER_PEARL, 1), new PriceInfo(4, 7)))

				.addTrade(1,
						new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.LIA_FALIA.ordinal()), new PriceInfo(12, 14)),
						new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.LIA_STILIA.ordinal()), new PriceInfo(12, 14)),
						new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.LIA_REGILIA.ordinal()), new PriceInfo(12, 14)),
						new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.ARLEY.ordinal()), new PriceInfo(16, 22)),
						new ListItemForEmeralds(new ItemStack(CONItems.itemEmeraldTablet, 1, ItemEmeraldTablet.EnumType.ARMES.ordinal()), new PriceInfo(26, 32))
						);

		GameRegistry.register(vprofArchaeologist);



		if (event.getSide().isClient())
		{
			Consumer<Item> simpleRegister = (item) -> ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			BiConsumer<Item, Integer> metaRegister = new BiConsumer<Item, Integer>()
			{
				@Override
				public void accept(Item item, Integer maxMeta)
				{
					for (int i=0; i<=maxMeta; ++i)
					{
						ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(item.getRegistryName().getResourceDomain(), item.getRegistryName().getResourcePath()), "inventory"));
					}
				}
			};
			BiConsumer<Item, String[]> arrayRegister = new BiConsumer<Item, String[]>()
			{
				@Override
				public void accept(Item item, String[] stringAry)
				{
					for (int i=0; i<stringAry.length; ++i)
					{
						ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(item.getRegistryName().getResourceDomain(), item.getRegistryName().getResourcePath() + (stringAry[i].equals("") ? "" : "_" + stringAry[i].toLowerCase())), "inventory"));
					}
				}
			};
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
			simpleRegister.accept(Item.getItemFromBlock(CONBlocks.blockRanimOre));
			simpleRegister.accept(Item.getItemFromBlock(CONBlocks.blockSanctuary));
			simpleRegister.accept(Item.getItemFromBlock(CONBlocks.blockInstantSanctuary));

			enumRegister.accept(CONItems.itemIntelligentStone, ItemIntelligentStone.EnumType.values());
			enumRegister.accept(CONItems.itemEmeraldTablet, ItemEmeraldTablet.EnumType.values());

			simpleRegister.accept(CONItems.itemYuleIngot);
			simpleRegister.accept(CONItems.itemTsafaIngot);
			simpleRegister.accept(CONItems.itemRanimIngot);
			metaRegister.accept(CONItems.itemFlyPod, ItemEntitySpawner.EnumType.values().length);
			metaRegister.accept(CONItems.itemInstantSanctuary, ItemEntitySpawner.EnumType.values().length);
			simpleRegister.accept(CONItems.itemSanctuaryRemover);

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiaAlter.class, new TESRLiaAlter());

			RenderingRegistry.registerEntityRenderingHandler(EntityFlyPod.class, RenderFlyPod::new);
			RenderingRegistry.registerEntityRenderingHandler(EntitySanctuary.class, RenderSanctuary::new);

			MinecraftForge.EVENT_BUS.register(MagicRenderTickEventHandler.INSTANCE);
		}

		MinecraftForge.ORE_GEN_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(MagicWorldTickEventHandler.INSTANCE);
	}


	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		PacketHandler.init();

		DataSerializers.registerSerializer(DATASERIALIZER_NBT);

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
		WorldGenerator genRanim = new WorldGenMinable(CONBlocks.blockRanimOre.getDefaultState(), 6);
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

				if(TerrainGen.generateOre(event.getWorld(), event.getRand(), genRanim, pos, OreGenEvent.GenerateMinable.EventType.CUSTOM))
					genStandardOre1.invoke(biomeDecorator, event.getWorld(), event.getRand(), 20, genRanim, 0, 96);

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}

	}

	@SubscribeEvent
	public void onSanctuaryPushEntity(SanctuaryPushEntityEvent event)
	{
		Entity entity = event.getEntity();
		ISanctuary sanctuary = event.getSanctuary();

		if (entity instanceof EntityXPOrb // TODO: ゲーム内部で設定したい
				|| entity instanceof EntityHanging
				|| entity instanceof INpc)
		{
			event.setCanceled(true);
		} else if (entity instanceof EntityThrowable)
		{
			event.setCanceled(sanctuary.isAllowedToEnter(((EntityThrowable) entity).getThrower()));
		} else if (entity instanceof EntityArrow)
		{
			event.setCanceled(sanctuary.isAllowedToEnter(((EntityArrow) entity).shootingEntity));
		} else if (entity instanceof IEntityOwnable)
		{
			event.setCanceled(sanctuary.isAllowedToEnter(((IEntityOwnable) entity).getOwner()));
		} else if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;

			if (player.capabilities.isCreativeMode) event.setCanceled(true);
		}

	}


}
