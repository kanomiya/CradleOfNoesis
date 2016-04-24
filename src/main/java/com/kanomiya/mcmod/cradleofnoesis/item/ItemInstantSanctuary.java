package com.kanomiya.mcmod.cradleofnoesis.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuary;
import com.kanomiya.mcmod.cradleofnoesis.api.sanctuary.ISanctuaryInfo;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntitySanctuary;

/**
 * @author Kanomiya
 *
 */
public class ItemInstantSanctuary extends ItemEntitySpawner
{
	public ItemInstantSanctuary()
	{
		super();

		setRegistryName(new ResourceLocation(CradleOfNoesisAPI.MODID, "itemInstantSanctuary"));
		setUnlocalizedName("itemInstantSanctuary");
		setCreativeTab(CradleOfNoesis.tab);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		List<ItemStack> subsubItems = Lists.newArrayList();
		super.getSubItems(itemIn, tab, subsubItems);

		for (Class<? extends ISanctuary> clazz: CradleOfNoesisAPI.getRegisteredSanctuaryClassSet())
		{
			Optional<ISanctuaryInfo> optSanctuaryInfo = CradleOfNoesisAPI.getSanctuaryInfo(clazz);

			if (optSanctuaryInfo.isPresent())
			{
				ISanctuary sanctuary = optSanctuaryInfo.get().createForInstantItem();

				if (sanctuary != null)
				{
					Optional<NBTTagCompound> optNbt = CradleOfNoesisAPI.serializeSanctuary(sanctuary);

					for (ItemStack stack: subsubItems)
					{
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

	@Override
	public Entity getEntity(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		NBTTagCompound nbtSanctuary = itemStackIn.getSubCompound(CradleOfNoesisAPI.DATAID_SANCTUARYSET, false);
		Optional<ISanctuary> optSanctuary = CradleOfNoesisAPI.deserializeSanctuary(nbtSanctuary);

		if (optSanctuary.isPresent())
		{
			ISanctuary sanctuary = optSanctuary.get();
			sanctuary.allowToEnter(playerIn); // TODO: ゲーム内で設定したい

			EntitySanctuary entity = new EntitySanctuary(worldIn, sanctuary);
			return entity;
		}

		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{

	}

}

