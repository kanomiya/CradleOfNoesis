package com.kanomiya.mcmod.cradleofnoesis.magic;

import com.kanomiya.mcmod.cradleofnoesis.api.CradleOfNoesisAPI;

public enum MagicNodeRender
{
	INSTANCE;

	public void doRender(IMagicNode node)
	{
		IMagicNodeShape shape = node.getShape();

		if (shape != null)
		{
			IMagicNodeShapeRender shapeRender = CradleOfNoesisAPI.magicNodeShapeRenderRegistry.get(shape.getClass());

			if (shapeRender != null)
			{
				shapeRender.doRender(shape);
			}

		}


	}

}
