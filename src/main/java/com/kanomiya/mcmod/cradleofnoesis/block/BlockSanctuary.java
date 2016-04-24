package com.kanomiya.mcmod.cradleofnoesis.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.google.common.base.Optional;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntitySanctuary;

/**
 * @author Kanomiya
 *
 */
public class BlockSanctuary extends BlockContainer
{
	public BlockSanctuary()
	{
		super(Material.ROCK);

		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		NBTTagCompound nbtSanctuary = stack.getSubCompound(CradleOfNoesisAPI.DATAID_SANCTUARYSET, false);

		if (nbtSanctuary != null)
		{
			TileEntity tile = worldIn.getTileEntity(pos);

			if (tile instanceof TileEntitySanctuary)
			{
				TileEntitySanctuary tileSanctuary = (TileEntitySanctuary) tile;
				Optional<ISanctuary> optSanctuary = CradleOfNoesisAPI.deserializeSanctuary(nbtSanctuary);

				if (optSanctuary.isPresent())
				{
					ISanctuary sanctuary = optSanctuary.get();
					sanctuary.allowToEnter(placer); // TODO: ゲーム内で設定したい

					EntitySanctuary entity = new EntitySanctuary(worldIn, sanctuary);
					entity.setPosition(pos.getX() +0.5d, pos.getY() +0.5d, pos.getZ() +0.5d);
					if (! worldIn.isRemote) worldIn.spawnEntityInWorld(entity);

					tileSanctuary.setSanctuaryEntity(entity);
				}
			}

		}

	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	/*
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		list.add(new ItemStack(itemIn));
	}
	*/

	/**
	* @inheritDoc
	*/
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySanctuary();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileEntitySanctuary)
		{
			TileEntitySanctuary tileSanctuary = (TileEntitySanctuary) tile;
			EntitySanctuary entitySanctuary = tileSanctuary.getSanctuaryEntity();

			if (entitySanctuary != null)
			{
				entitySanctuary.setDead();
			}

		}

		super.breakBlock(worldIn, pos, state);
	}

}
