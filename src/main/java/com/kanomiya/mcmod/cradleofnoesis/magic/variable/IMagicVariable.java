package com.kanomiya.mcmod.cradleofnoesis.magic.variable;



/**
 * @author Kanomiya
 *
 */
public interface IMagicVariable<T>
{
	T getValue(IMagicVariableType<T> type);

}
