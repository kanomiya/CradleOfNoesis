package com.kanomiya.mcmod.cradleofnoesis.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;

/**
 * @author Kanomiya
 *
 */
public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(CradleOfNoesis.MODID);

	public static void init()
	{
		int id = -1;
		INSTANCE.registerMessage(MessageMagicStatusEntity.Handler.class, MessageMagicStatusEntity.class, ++id, Side.CLIENT);

	}

}
