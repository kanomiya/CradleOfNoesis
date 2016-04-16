package com.kanomiya.mcmod.cradleofnoesis.block;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;

/**
 * @author Kanomiya
 *
 */
public class BlockSimpleOre extends BlockOre
{
	protected Function<Random, Integer> expDrop;

	public BlockSimpleOre()
	{
		this(Material.rock.getMaterialMapColor());
	}

	public BlockSimpleOre(MapColor color)
	{
		super(color);
		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		if (expDrop == null) return super.getExpDrop(state, world, pos, fortune);

		Random rand = world instanceof World ? ((World)world).rand : new Random();
		return expDrop.apply(rand);
	}

	public Block setExpDrop(Function<Random, Integer> expDrop)
	{
		this.expDrop = expDrop;
		return this;
	}

}
