package com.kanomiya.mcmod.cradleofnoesis.magic;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.living.LivingEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesis;
import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.event.UpdateEventHandler;
import com.kanomiya.mcmod.cradleofnoesis.magic.effect.IMagicEffect;
import com.kanomiya.mcmod.cradleofnoesis.network.MessageMagicStatusEntity;
import com.kanomiya.mcmod.cradleofnoesis.network.PacketHandler;

/**
 * @author Kanomiya
 *
 */
public class MagicStatus<O extends ICapabilityProvider> implements ICapabilityProvider, INBTSerializable<NBTTagCompound>, ITickable {
	public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(CradleOfNoesis.MODID, "magicStatus");

	public static <O extends ICapabilityProvider> MagicStatus<O> createDefault()
	{
		return new MagicStatus<O>();
	}

	public static <O extends ICapabilityProvider> MagicStatus<O> create(int mp, int mpCapacity, O owner)
	{
		MagicStatus<O> magicStatus = new MagicStatus<O>(owner);
		magicStatus.mpCapacity = mpCapacity;
		magicStatus.mp = mp;

		return magicStatus;
	}

	public static <O extends ICapabilityProvider> MagicStatus<O> create(int mpCapacity, boolean fullMp, O owner)
	{
		MagicStatus<O> magicStatus = new MagicStatus<O>(owner);
		magicStatus.mpCapacity = mpCapacity;
		if (fullMp) magicStatus.fullMp();

		return magicStatus;
	}


	protected int mp, mpCapacity;
	protected boolean updated;
	protected O owner;
	protected Map<ResourceLocation, IMagicEffect> magicEffects;
	protected List<ITickable> updateListeners;

	protected MagicStatus()
	{
		updateListeners = Lists.newArrayList();
	}

	protected MagicStatus(O owner)
	{
		this();

		this.owner = owner;
	}



	/**
	 * MPを取得する / Get MP value
	 * @return MP
	 */
	public int getMp()
	{
		return mp;
	}

	/**
	 * MPの最大量を取得する / Get MP capacity value
	 * @return
	 */
	public int getMpCapacity()
	{
		return mpCapacity;
	}

	public boolean isUpdated()
	{
		return updated;
	}

	/**
	 *
	 * MPを受容する / Accept MP
	 *
	 * @param donor 供与者 / donor
	 * @param amount 供与量 / donating value
	 */
	public void acceptMp(MagicStatus donor, int amount)
	{
		int actualAmount = Math.min(Math.min(amount, getFreeMpCapacity()), donor.getMp());
		donor.releaseMp(actualAmount);
		acceptMp(actualAmount);
	}

	/**
	 *
	 * MPを受容する
	 *
	 * @param amount 受容するMP
	 * @return 与えられたMPの余り / The rest of giving MP
	 */
	public int acceptMp(int amount)
	{
		int actualAmount = Math.min(amount, getFreeMpCapacity());
		mp += actualAmount;

		updated = true;
		return amount -actualAmount;
	}

	/**
	 *
	 * MPを放出する
	 *
	 * @param amount 放出するMP
	 * @return 足りないMP / The MP shortage
	 */
	public int releaseMp(int amount)
	{
		int actualAmount = Math.min(amount, getMp());
		mp -= actualAmount;

		updated = true;
		return amount -actualAmount;
	}

	public void fullMp()
	{
		mp = mpCapacity;
		updated = true;
	}

	public void releaseAllMp()
	{
		mp = 0;
		updated = true;
	}

	/**
	 *
	 * MP容量の空き容量を取得する (どれだけのMPが受容可能か)
	 * Get free space size of MP capacity (= how much MP can be accepted)
	 *
	 * @return MP容量の空き領域 / Free space size of MP capacity
	 */
	public int getFreeMpCapacity()
	{
		return getMpCapacity() -getMp();
	}

	/**
	 * MPが満タンがどうか / Whether MP is full
	 * @return MPが満タンならtrue / if MP is full, true
	 */
	public boolean isMpFull()
	{
		return getMpCapacity() <= getMp();
	}


	public boolean hasOwner()
	{
		return getOwner() != null;
	}

	public O getOwner()
	{
		return owner;
	}

	public boolean hasNoEffect()
	{
		return magicEffects == null || magicEffects.isEmpty();
	}

	public boolean hasEffect(ResourceLocation key)
	{
		return magicEffects != null && magicEffects.containsKey(key);
	}

	/**
	 * @param key
	 * @param magicEffect
	 */
	public void addEffect(ResourceLocation key, IMagicEffect magicEffect)
	{
		if (magicEffects == null) magicEffects = Maps.newHashMap();
		magicEffects.put(key, magicEffect);
		if (magicEffect instanceof ITickable) updateListeners.add((ITickable) magicEffect);
		updated = true;
	}

	/**
	 * @param key
	 */
	public void removeEffect(ResourceLocation key)
	{
		if (magicEffects == null) return ;
		updateListeners.remove(magicEffects.remove(key));
		updated = true;
	}

	public void clearEffect()
	{
		if (magicEffects == null) return ;

		magicEffects.values().stream().filter(effect -> effect instanceof ITickable).forEach(updateListeners::remove);
		magicEffects.clear();
		updated = true;
	}



	/**
	*
	* For Entity // TileEntity (implements ITickableWithMagicStatus)
	*
	* @see UpdateEventHandler#onLivingUpdate(LivingEvent.LivingUpdateEvent)
	* @see ITickableWithMagicStatus.Item#onUpdate(ItemStack, World, Entity, int, boolean)
	* @see ITickableWithMagicStatus.Item#onEntityItemUpdate(EntityItem)
	* @see ITickableWithMagicStatus.TileEntity#update()
	*
	* @inheritDoc
	*/
	@Override
	public void update()
	{
		updateListeners.forEach(ITickable::update);

		// magicEffects.values().stream().filter(effect -> effect instanceof ITickable).map(effect -> (ITickable) effect).forEach(ITickable::update);

		if (isUpdated())
		{
			if (owner instanceof EntityLivingBase)
			{
				PacketHandler.INSTANCE.sendToAll(new MessageMagicStatusEntity((EntityLivingBase) owner, this));
				updated = false;
			}

			/*
			 * TileEntity#onDataPacket(NetworkManager, S35PacketUpdateTileEntity)
			 * @see ITickableWithMagicStatus.Item#onUpdate(ItemStack, World, Entity, int, boolean)
			 * @see ITickableWithMagicStatus.Item#onEntityItemUpdate(EntityItem)
			 */
		}

	}


	/**
	* @inheritDoc
	*/
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setInteger("mp", mp);
		nbt.setInteger("mpCapacity", mpCapacity);

		if (! hasNoEffect())
		{
			NBTTagCompound nbtEffects = new NBTTagCompound();
			Iterator<ResourceLocation> keyItr = magicEffects.keySet().iterator();

			while (keyItr.hasNext())
			{
				ResourceLocation key = keyItr.next();
				nbtEffects.setTag(key.toString(), magicEffects.get(key).serializeNBT());
			}

			nbt.setTag("effects", nbtEffects);
		}

		return nbt;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		mp = nbt.getInteger("mp");
		mpCapacity = nbt.getInteger("mpCapacity");

		if (! hasNoEffect())
		{
			NBTTagCompound nbtEffects = new NBTTagCompound();
			Iterator<ResourceLocation> keyItr = magicEffects.keySet().iterator();

			while (keyItr.hasNext())
			{
				ResourceLocation key = keyItr.next();
				magicEffects.get(key).deserializeNBT(nbtEffects.getCompoundTag(key.toString()));
			}

			nbt.setTag("effects", nbtEffects);
		}

	}

	public static class Storage implements Capability.IStorage<MagicStatus>
	{
		/**
		* @inheritDoc
		*/
		@Override
		public NBTBase writeNBT(Capability<MagicStatus> capability, MagicStatus instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		/**
		* @inheritDoc
		*/
		@Override
		public void readNBT(Capability<MagicStatus> capability, MagicStatus instance, EnumFacing side, NBTBase nbt)
		{
			if (nbt instanceof NBTTagCompound)
			{
				instance.deserializeNBT((NBTTagCompound) nbt);
			}
		}

	}



	/**
	* @inheritDoc
	*/
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return (capability == CradleOfNoesisAPI.capMagicStatus);
	}



	/**
	* @inheritDoc
	*/
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability != CradleOfNoesisAPI.capMagicStatus) return null;
		return (T) this;
	}




}
