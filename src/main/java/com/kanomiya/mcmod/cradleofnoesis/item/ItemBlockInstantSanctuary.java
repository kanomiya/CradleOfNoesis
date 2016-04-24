package com.kanomiya.mcmod.cradleofnoesis.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Optional;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuaryInfo;

/**
 * @author Kanomiya
 *
 */
public class ItemBlockInstantSanctuary extends ItemBlock
{
	public ItemBlockInstantSanctuary(Block block)
	{
		super(block);

		setCreativeTab(CradleOfNoesis.tab);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (Class<? extends ISanctuary> clazz: CradleOfNoesisAPI.getRegisteredSanctuaryClassSet())
		{
			Optional<ISanctuaryInfo> optSanctuaryInfo = CradleOfNoesisAPI.getSanctuaryInfo(clazz);

			if (optSanctuaryInfo.isPresent())
			{
				ISanctuary sanctuary = optSanctuaryInfo.get().createForInstantBlock();

				if (sanctuary != null)
				{
					ItemStack stack = new ItemStack(itemIn);
					Optional<NBTTagCompound> optNbt = CradleOfNoesisAPI.serializeSanctuary(sanctuary);

					if (optNbt.isPresent())
					{
						stack.setTagInfo(CradleOfNoesisAPI.DATAID_SANCTUARYSET, optNbt.get());
					}

					subItems.add(stack);
				}
			}
		}

	}


}
