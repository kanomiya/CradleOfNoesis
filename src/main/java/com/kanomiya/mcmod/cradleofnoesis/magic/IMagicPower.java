package com.kanomiya.mcmod.cradleofnoesis.magic;

/**
 * @author Kanomiya
 *
 */
public interface IMagicPower
{
	int getAmount();

	void incr(int incrAmount);
	void decr(int decrAmount);

}
