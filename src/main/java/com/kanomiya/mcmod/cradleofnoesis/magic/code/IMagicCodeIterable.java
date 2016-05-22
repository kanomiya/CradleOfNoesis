package com.kanomiya.mcmod.cradleofnoesis.magic.code;

import com.kanomiya.mcmod.cradleofnoesis.magic.parser.MagicCodeIterator;

/**
 * @author Kanomiya
 *
 */
public interface IMagicCodeIterable extends IMagicCode, Iterable<IMagicCode>
{
	@Override
	MagicCodeIterator iterator();//Fluid Block

}
