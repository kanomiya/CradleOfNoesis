package com.kanomiya.mcmod.cradleofnoesis.magic.matter.type;

import java.util.function.Function;

import com.kanomiya.mcmod.cradleofnoesis.magic.matter.state.MagicMatterState;


/**
 * @author Kanomiya
 *
 */
public class SimpleMagicMatterType implements IMagicMatterType {

	protected String unlocalizedName;
	protected Function<MagicMatterState, Float> dropChance;

	public SimpleMagicMatterType(String unlocalizedName, Function<MagicMatterState, Float> dropChance)
	{
		this.unlocalizedName = unlocalizedName;
		this.dropChance = dropChance;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public float getDropChance(MagicMatterState state)
	{
		return dropChance.apply(state);
	}

	/**
	* @inheritDoc
	*/
	@Override
	public String getUnlocalizedName(MagicMatterState state) {
		return unlocalizedName;
	}


}
