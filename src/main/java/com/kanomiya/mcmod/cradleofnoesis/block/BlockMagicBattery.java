package com.kanomiya.mcmod.cradleofnoesis.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.energy.IHasHandyEnergy;
import com.kanomiya.mcmod.cradleofnoesis.tileentity.TileEntityMagicBattery;
import com.kanomiya.mcmod.energyway.api.EnergyWayAPI;
import com.kanomiya.mcmod.energyway.api.energy.Energy;
import com.kanomiya.mcmod.energyway.api.props.EntityPropertiesEnergy;

/**
 * @author Kanomiya
 *
 */
public class BlockMagicBattery extends BlockContainer {

	public BlockMagicBattery()
	{
		super(Material.rock);

		setRegistryName(CradleOfNoesis.MODID, "blockMagicBattery");
		setUnlocalizedName("blockMagicBattery");
		setCreativeTab(CradleOfNoesis.tab);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		EntityPropertiesEnergy props = EnergyWayAPI.getProperties(playerIn);

		if (tile instanceof IHasHandyEnergy && props != null)
		{
			IHasHandyEnergy iHasHandyEnergy = (IHasHandyEnergy) tile;

			if (iHasHandyEnergy.hasEnergy(CradleOfNoesis.energyTypeMagic))
			{
				if (! worldIn.isRemote)
				{
					iHasHandyEnergy.doHandyCharge(CradleOfNoesis.energyTypeMagic, props);

					Energy energy = iHasHandyEnergy.getEnergy(CradleOfNoesis.energyTypeMagic);

					if (100 <= energy.getAmount())
					{
						worldIn.createExplosion(playerIn, pos.getX(), pos.getY(), pos.getZ(), 3.0f, true);
						Energy.VOID.accept(iHasHandyEnergy, CradleOfNoesis.energyTypeMagic, 100);
					}

					playerIn.addChatMessage(new ChatComponentText(energy.getAmount() + "/" + energy.getCapacity()));
				}

				return true;
			}

		}


		return false;
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
		return new TileEntityMagicBattery();
	}



}
