package com.kanomiya.mcmod.cradleofnoesis;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;

/**
 * @author Kanomiya
 *
 */
public class CradleOfNoesisAPI {
	public static final ResourceLocation RL_MAGICSTATUS = new ResourceLocation(CradleOfNoesis.MODID, "magicStatus");

	@CapabilityInject(MagicStatus.class)
	public static final Capability<MagicStatus> capMagicStatus = null;


}
