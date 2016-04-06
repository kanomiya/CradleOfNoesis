package com.kanomiya.mcmod.cradleofnoesis;

import com.kanomiya.mcmod.cradleofnoesis.magic.matter.type.IMagicMatterType;
import com.kanomiya.mcmod.cradleofnoesis.magic.matter.type.SimpleMagicMatterType;

public class CONMagicMatterTypes
{
	public static IMagicMatterType UNKNOWN = new SimpleMagicMatterType("cradleofnoesis.matter.unknown", (state) -> (0f));
	public static IMagicMatterType YULE = new SimpleMagicMatterType("cradleofnoesis.matter.yule", (state) -> (1f));
	public static IMagicMatterType TSAFA = new SimpleMagicMatterType("cradleofnoesis.matter.tsafa", (state) -> (1f));

}
