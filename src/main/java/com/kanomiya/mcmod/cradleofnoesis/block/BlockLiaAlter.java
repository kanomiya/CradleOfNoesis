package com.kanomiya.mcmod.cradleofnoesis.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis.CONGuis;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityLiaAlter;

/**
 * @author Kanomiya
 *
 */
public class BlockLiaAlter extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockLiaAlter()
	{
		super(Material.rock);

		setRegistryName(CradleOfNoesis.MODID, "blockLiaAlter");
		setUnlocalizedName("blockLiaAlter");
		setCreativeTab(CradleOfNoesis.tab);
		setBlockBounds(0.0f, 0.0f, 0f, 1.0f, 0.5f, 1.0f);

	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileEntityLiaAlter)
		{
			playerIn.openGui(CradleOfNoesis.instance, CONGuis.GUIID_LIAALTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}

		return false;
	}

	@Override public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getStateFromMeta(meta).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityLiaAlter();
	}


	@Override public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex();
	}


	@Override protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING });
	}

	@Override public boolean isOpaqueCube() { return false; }

	@Override public boolean isFullCube() { return false; }

	@Override public boolean isFullBlock() { return false; }

	@Override public boolean getUseNeighborBrightness() { return true; }



}
