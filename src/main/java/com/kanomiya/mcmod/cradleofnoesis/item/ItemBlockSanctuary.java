package com.kanomiya.mcmod.cradleofnoesis.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.sanctuary.HealSanctuary;

/**
 * @author Kanomiya
 *
 */
public class ItemBlockSanctuary extends ItemBlock
{
	public ItemBlockSanctuary(Block block)
	{
		super(block);
		setUnlocalizedName("itemBlockSanctuary");

		setCreativeTab(CradleOfNoesis.tab);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		{
			ISanctuary sanctuary = new HealSanctuary(5.0f, Short.MAX_VALUE, 0.1f, 5);
			ItemStack stack = new ItemStack(itemIn);
			NBTTagCompound compound = new NBTTagCompound();

			ResourceLocation id = CradleOfNoesisAPI.SANCUTUARY_REGISTRY.inverse().get(sanctuary.getClass());
			if (id != null)
			{
				compound.setString("sanctuaryId", id.toString());
				compound.setTag("sanctuary", sanctuary.serializeNBT());

			}


			stack.setTagInfo(CradleOfNoesisAPI.MODID + ":sanctuary", compound);

			subItems.add(stack);
		}

	}


}
