package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

// Date: 2016/04/01 20:09:31
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX


public class ModelEnderPearl extends ModelBase
{
	//fields
	ModelRenderer enderPearl;

	public ModelEnderPearl()
	{
		textureWidth = 16;
		textureHeight = 16;

		enderPearl = new ModelRenderer(this, 0, 0);
		enderPearl.addBox(0F, 0F, 0F, 2, 2, 2);
		enderPearl.setRotationPoint(0F, 0F, 0F);
		enderPearl.setTextureSize(16, 16);
	}

	public void doRender(float f) {
		enderPearl.render(f);
	}


}
