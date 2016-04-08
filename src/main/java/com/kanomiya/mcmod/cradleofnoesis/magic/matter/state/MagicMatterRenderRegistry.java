package com.kanomiya.mcmod.cradleofnoesis.magic.matter.state;

import java.util.List;
import java.util.function.Function;

import net.minecraft.client.renderer.entity.Render;

import com.google.common.collect.Lists;
import com.kanomiya.mcmod.cradleofnoesis.entity.EntityMagicMatter;


/**
 * @author Kanomiya
 *
 */
public class MagicMatterRenderRegistry {
	public static final MagicMatterRenderRegistry INSTANCE = new MagicMatterRenderRegistry();

	protected List<Function<EntityMagicMatter, Render<EntityMagicMatter>>> funcList;

	protected MagicMatterRenderRegistry()
	{
		funcList = Lists.newArrayList();
	}

	public void register(Function<EntityMagicMatter, Render<EntityMagicMatter>> func)
	{
		funcList.add(func);
	}

	public Render<EntityMagicMatter> getRender(EntityMagicMatter entityMagicMatter)
	{
		for (Function<EntityMagicMatter, Render<EntityMagicMatter>> func: funcList)
		{
			Render<EntityMagicMatter> render = func.apply(entityMagicMatter);
			if (render != null) return render;
		}

		return null;
	}

	public int size()
	{
		return funcList.size();
	}

}
