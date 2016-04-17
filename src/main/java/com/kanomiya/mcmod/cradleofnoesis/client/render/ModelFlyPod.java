package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFlyPod extends ModelBase
{
	//fields
	ModelRenderer base;

	public ModelFlyPod()
	{
		textureWidth = 64;
		textureHeight = 64;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(0F, 0F, 0F, 16, 16, 16);
		base.setRotationPoint(0F, 0F, 0F);
		base.setTextureSize(64, 64);
	}

	@Override
	public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		setRotationAngles(p_78088_2_, limbSwing, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		doRender(scale);
	}

	public void doRender(float f) {
		base.render(f);
	}


}
