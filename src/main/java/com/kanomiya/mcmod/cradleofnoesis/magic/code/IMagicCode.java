package com.kanomiya.mcmod.cradleofnoesis.magic.code;

/**
 * @author Kanomiya
 *
 */
public interface IMagicCode
{
	default boolean canExecute(IMagicCode prev, IMagicCode next)
	{
		return true;
	}


}
