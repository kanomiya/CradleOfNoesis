package com.kanomiya.mcmod.cradleofnoesis.magic.parser;

import java.util.Iterator;

import com.kanomiya.mcmod.cradleofnoesis.magic.code.IMagicCode;
import com.kanomiya.mcmod.cradleofnoesis.magic.code.IMagicCodeIterable;

/**
 * @author Kanomiya
 *
 */
public class MagicCodeIterator implements Iterator<IMagicCode>
{
	protected IMagicCode[] codes;
	protected int nextLine;
	protected MagicCodeIterator childItr;

	public MagicCodeIterator(IMagicCode... codes)
	{
		this.codes = codes;
	}

	/**
	* @inheritDoc
	*/
	@Override
	public boolean hasNext()
	{
		return (childItr == null && nextLine < codes.length) || childItr.hasNext();
	}

	/**
	* @inheritDoc
	*/
	@Override
	public IMagicCode next()
	{
		if (childItr != null)
		{
			if (childItr.hasNext())
			{
				return childItr.next();
			} else childItr = null;
		}

		IMagicCode next = codes[nextLine];

		if (next instanceof IMagicCodeIterable)
		{
			childItr = ((IMagicCodeIterable) next).iterator();

			if (childItr.hasNext())
			{
				++ nextLine;
				return childItr.next();
			} else childItr = null;
		}

		++ nextLine;
		return next;
	}

}
