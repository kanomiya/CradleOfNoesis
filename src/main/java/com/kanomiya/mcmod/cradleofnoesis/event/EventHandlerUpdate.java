package com.kanomiya.mcmod.cradleofnoesis.event;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.energyway.api.EnergyWayAPI;
import com.kanomiya.mcmod.energyway.api.energy.Energy;
import com.kanomiya.mcmod.energyway.api.props.EntityPropertiesEnergy;


/**
 * @author Kanomiya
 *
 */
public class EventHandlerUpdate {

	public static final EventHandlerUpdate INSTANCE = new EventHandlerUpdate();

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		Entity entity = event.entity;
		IExtendedEntityProperties props = entity.getExtendedProperties(EnergyWayAPI.ID_DATA);

		if (props instanceof EntityPropertiesEnergy)
		{
			EntityPropertiesEnergy propsE = (EntityPropertiesEnergy) props;

			if (propsE.hasEnergy(CradleOfNoesis.energyTypeMagic))
			{
				if (! propsE.hasCustomData()) propsE.setCustomData(new NBTTagCompound());
				NBTTagCompound customData = propsE.getCustomData();

				int interval = customData.hasKey("magicHealInterval") ? customData.getInteger("magicHealInterval") : 0;
				++interval;

				if (70 <= interval)
				{
					if (! entity.worldObj.isRemote)
					{
						Energy energyMagic = propsE.getEnergy(CradleOfNoesis.energyTypeMagic);

						if (! energyMagic.isFull())
						{
							energyMagic.accept(Energy.createUnlimited(CradleOfNoesis.energyTypeMagic, 1), 1);
						}
					}

					interval = 0;
				}

				customData.setInteger("magicHealInterval", interval);
			}

		}

	}

}
