package com.kanomiya.mcmod.cradleofnoesis.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;

/**
 * @author Kanomiya
 *
 */
public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(CradleOfNoesisAPI.MODID);

	public static void init()
	{
		int id = -1;

	}

}
