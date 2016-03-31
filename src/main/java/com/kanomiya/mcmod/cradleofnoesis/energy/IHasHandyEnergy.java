package com.kanomiya.mcmod.cradleofnoesis.energy;

import com.kanomiya.mcmod.energyway.api.energy.EnergyType;
import com.kanomiya.mcmod.energyway.api.energy.IHasEnergy;



/**
 *
 * 使用することでエネルギーを受け取る
 *
 * @author Kanomiya
 *
 */
public interface IHasHandyEnergy extends IHasEnergy {

	/**
	 *
	 * 可能ならばエネルギーを受容する
	 *
	 * @param energyType エネルギータイプ
	 * @param donor 供与者
	 * @return エネルギーの受容に不完全でも成功したかどうか
	 */
	default boolean doHandyCharge(EnergyType energyType, IHasEnergy donor)
	{
		if (! canAccept(energyType, donor)) return false;

		int amount = getOnceChargeAmount(energyType, donor);
		accept(donor, energyType, amount);
		return true;
	}


	/**
	 *
	 * エネルギーを受容する能力があるかどうかを判定する
	 *
	 * @param energyType エネルギータイプ
	 * @param donor 供与者
	 * @return エネルギーを受容する能力があるかどうか
	 */
	boolean canAccept(EnergyType energyType, IHasEnergy donor);

	/**
	 *
	 * 一度に供与者から受け取るエネルギー量を返す
	 *
	 * @param energyType エネルギータイプ
	 * @param donor 供与者
	 * @return 一度に供与者から受け取るエネルギー量
	 */
	int getOnceChargeAmount(EnergyType energyType, IHasEnergy donor);


}
