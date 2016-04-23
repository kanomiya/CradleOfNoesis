package com.kanomiya.mcmod.cradleofnoesis.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

		setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "blockSanctuary"));
		setUnlocalizedName("blockSanctuary");
		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		NBTTagCompound nbtSanctuary = stack.getSubCompound(CradleOfNoesisAPI.MODID + ":sanctuary", false);

		if (nbtSanctuary != null)
		{
			TileEntity tile = worldIn.getTileEntity(pos);

			if (tile instanceof TileEntitySanctuary)
			{
				TileEntitySanctuary tileSanctuary = (TileEntitySanctuary) tile;
				ISanctuary sanctuary = CradleOfNoesisAPI.createSanctuaryInstance(new ResourceLocation(nbtSanctuary.getString("sanctuaryId")));

				if (sanctuary != null)
				{
					sanctuary.deserializeNBT(nbtSanctuary.getCompoundTag("sanctuary"));
				}

				sanctuary.allowToEnter(placer); // TODO: ゲーム内で設定したい

				EntitySanctuary entity = new EntitySanctuary(worldIn, sanctuary);
				entity.setPosition(pos.getX() +0.5d, pos.getY() +0.5d, pos.getZ() +0.5d);
				if (! worldIn.isRemote) worldIn.spawnEntityInWorld(entity);

				tileSanctuary.setSanctuaryEntity(entity);
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

			if (tileSanctuary.getSanctuaryEntity() != null)
			{
				tileSanctuary.getSanctuaryEntity().setDead();
			}

		}

		super.breakBlock(worldIn, pos, state);
	}

}
