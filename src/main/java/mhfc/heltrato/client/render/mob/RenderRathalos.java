package mhfc.heltrato.client.render.mob;

import mhfc.heltrato.client.model.mob.boss.ModelRathalos;
import mhfc.heltrato.common.entity.mob.EntityRathalos;
import mhfc.heltrato.common.util.lib.MHFCReference;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRathalos extends RenderLiving {
	private float scale;
	protected ModelRathalos mainModel;

	public RenderRathalos(ModelRathalos par1ModelBase, float par2, float par3) {
		super(par1ModelBase, par2 * par3);
		this.scale = par3;
	}

	/**
	 * Applies the scale to the transform matrix
	 */
	protected void preRenderScale(EntityRathalos par1, float par2) {
		GL11.glScalef(this.scale, this.scale, this.scale);
	}

	protected ResourceLocation func_110870_a(EntityRathalos par1) {
		return new ResourceLocation(MHFCReference.mob_rathalos_tex);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase,
			float partialTick) {
		this.mainModel.setPartialTick(partialTick);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.func_110870_a((EntityRathalos) par1Entity);
	}

}