package com.kanomiya.mcmod.cradleofnoesis.magic.code;

import com.kanomiya.mcmod.cradleofnoesis.magic.parser.MagicCodeIterator;


/**
 * @author Kanomiya
 *
 */
public class MagicCodeBlock implements IMagicCodeIterable
{
	protected IMagicCode[] codes;

	public MagicCodeBlock(IMagicCode... codes)
	{
		this.codes = codes;
	}


	/**
	* @inheritDoc
	*/
	@Override
	public MagicCodeIterator iterator()
	{
		return new MagicCodeIterator(codes);
	}


}
