package com.kanomiya.mcmod.cradleofnoesis.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityMagicBattery;

/**
 * @author Kanomiya
 *
 */
public class BlockMagicBattery extends BlockContainer {

	public BlockMagicBattery()
	{
		super(Material.rock);

		setRegistryName(new ResourceLocation(CradleOfNoesis.MODID, "blockMagicBattery"));
		setUnlocalizedName("blockMagicBattery");
		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile != null)
		{
			MagicStatus msTile = tile.getCapability(CradleOfNoesisAPI.capMagicStatus, null);
			MagicStatus msPlayer = playerIn.getCapability(CradleOfNoesisAPI.capMagicStatus, null);

			if (! worldIn.isRemote)
			{
				msTile.acceptMp(msPlayer, 10);

				if (msTile.isMpFull())
				{
					worldIn.createExplosion(playerIn, pos.getX(), pos.getY(), pos.getZ(), 3.0f, true);
					msTile.releaseAllMp();
				}

				playerIn.addChatMessage(new TextComponentString(msTile.getMp() + "/" + msTile.getMpCapacity()));
			}

			return true;
		}


		return false;
	}


	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMagicBattery();
	}



}
