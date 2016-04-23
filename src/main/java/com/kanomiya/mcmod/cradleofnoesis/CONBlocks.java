package com.kanomiya.mcmod.cradleofnoesis;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.block.BlockLiaAlter;
import com.kanomiya.mcmod.cradleofnoesis.block.BlockSanctuary;
import com.kanomiya.mcmod.cradleofnoesis.block.BlockSimpleOre;

public class CONBlocks
{
	public static BlockLiaAlter blockLiaAlter = new BlockLiaAlter();
	public static Block blockYuleOre = new BlockSimpleOre().setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "blockYuleOre")).setUnlocalizedName("blockYuleOre").setHardness(3.0F).setResistance(5.0F);
	public static Block blockTsafaOre = new BlockSimpleOre().setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "blockTsafaOre")).setUnlocalizedName("blockTsafaOre").setHardness(3.0F).setResistance(5.0F);
	public static BlockSanctuary blockSanctuary = new BlockSanctuary();

}
