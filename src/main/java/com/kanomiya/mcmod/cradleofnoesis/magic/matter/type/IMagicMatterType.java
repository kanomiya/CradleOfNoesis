package com.kanomiya.mcmod.cradleofnoesis.magic.matter.type;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.state.MagicMatterState;

/**
 * @author Kanomiya
 *
 */
public interface IMagicMatterType {
	public static final ResourceLocation RL_UNKNOWN = new ResourceLocation(CradleOfNoesis.MODID, "unknown");

	/**
	 * @return
	 */
	float getDropChance(MagicMatterState state);

	/**
	 * @return
	 */
	String getUnlocalizedName(MagicMatterState state);

	/**
	 * @return
	 */
	default String getDisplayName(MagicMatterState state)
	{
		return I18n.translateToLocal(getUnlocalizedName(state));
	}


}
