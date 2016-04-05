package com.kanomiya.mcmod.cradleofnoesis.magic;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;

/**
 * @author Kanomiya
 *
 */
public interface IHasMagicStatus<T extends ICapabilityProvider> {

	default MagicStatus getMagicStatus(T provider)
	{
		if (provider == null) return null;
		if (! provider.hasCapability(CradleOfNoesisAPI.capMagicStatus, null)) return null;

		return provider.getCapability(CradleOfNoesisAPI.capMagicStatus, null);
	}


}
