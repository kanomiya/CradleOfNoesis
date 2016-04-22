package com.kanomiya.mcmod.cradleofnoesis.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kanomiya.mcmod.cradleofnoesis.entity.EntityFlyPod;

@SideOnly(Side.CLIENT)
public class ModelFlyPod extends ModelBase
{
	protected ModelRenderer topBoard;
	protected ModelRenderer stone;
	protected ModelRenderer bottomBoard;
	protected ModelRenderer gun1;
	protected ModelRenderer gun2;
	protected ModelRenderer leg1;
	protected ModelRenderer leg2;
	protected ModelRenderer pillar;

	public ModelFlyPod()
	{
		textureWidth = 64;
		textureHeight = 64;
		topBoard = new ModelRenderer(this, 0, 0);
		topBoard.addBox(0F, 0F, 0F, 11, 2, 11);
		topBoard.setRotationPoint(3F, 10F, 3F);
		topBoard.setTextureSize(44, 13);

		stone = new ModelRenderer(this, 0, 48);
		stone.addBox(0F, 0F, 0F, 3, 3, 3);
		stone.setRotationPoint(7F, 7F, 7F);
		stone.setTextureSize(12, 6);

		bottomBoard = new ModelRenderer(this, 0, 0);
		bottomBoard.addBox(0F, 0F, 0F, 11, 2, 11);
		bottomBoard.setRotationPoint(3F, 5F, 3F);
		bottomBoard.setTextureSize(44, 13);

		gun1 = new ModelRenderer(this, 0, 16);
		gun1.addBox(0F, 0F, 0F, 1, 1, 2);
		gun1.setRotationPoint(6F, 6F, 1F);
		gun1.setTextureSize(8, 3);

		gun2 = new ModelRenderer(this, 0, 16);
		gun2.addBox(0F, 0F, 0F, 1, 1, 2);
		gun2.setRotationPoint(10F, 6F, 1F);
		gun2.setTextureSize(8, 3);

		leg1 = new ModelRenderer(this, 16, 16);
		leg1.addBox(0F, 0F, 0F, 1, 5, 1);
		leg1.setRotationPoint(6F, 0F, 8F);
		leg1.setTextureSize(64, 64);

		leg2 = new ModelRenderer(this, 16, 16);
		leg2.addBox(0F, 0F, 0F, 1, 5, 1);
		leg2.setRotationPoint(10F, 0F, 8F);
		leg2.setTextureSize(64, 64);

		pillar = new ModelRenderer(this, 16, 48);
		pillar.addBox(0F, 0F, 0F, 1, 3, 1);
		pillar.setTextureSize(4, 4);


	}

	@Override
	public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		topBoard.render(scale);

		if (entityIn instanceof EntityFlyPod && ((EntityFlyPod) entityIn).getStoneStack() != null)
		{
			stone.render(scale);
		}

		bottomBoard.render(scale);
		gun1.render(scale);
		gun2.render(scale);
		leg1.render(scale);
		leg2.render(scale);

		for (int i=0; i<6; ++i)
		{
			pillar.setRotationPoint(3F, 7F, 3F +i*2);
			pillar.render(scale);
			pillar.setRotationPoint(13F, 7F, 3F +i*2);
			pillar.render(scale);

			if (0 < i && i < 5)
			{
				pillar.setRotationPoint(3F +i*2, 7F, 13F);
				pillar.render(scale);

				pillar.setRotationPoint(3F +i*2, 7F, 3F);
				pillar.render(scale);

			}
		}

	}


}
