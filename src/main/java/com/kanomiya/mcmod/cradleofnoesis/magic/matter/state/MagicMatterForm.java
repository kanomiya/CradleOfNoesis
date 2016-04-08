package com.kanomiya.mcmod.cradleofnoesis.magic.matter.state;

import net.minecraft.util.text.translation.I18n;



/**
 * @author Kanomiya
 *
 */
public enum MagicMatterForm {
	BLOCK,
	STACKED,

	;

	public String getUnlocalizedName()
	{
		return "cradleofnoesis.matter.form." + name().toLowerCase();
	}

	public String getDisplayName()
	{
		return I18n.translateToLocal(getUnlocalizedName());
	}

}
