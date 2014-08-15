package mhfc.heltrato.client.render.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import mhfc.heltrato.common.entity.mob.EntityAptonoth;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAptonoth extends RenderLiving
{
	private float scale;
	private static final ResourceLocation texture = new ResourceLocation("mhfc:textures/mobs/aptonoth.png");
	

    public RenderAptonoth(ModelBase par1ModelBase, float par2, float par3)
    {
        super(par1ModelBase, par2 * par3);
        this.scale = par3;
    }

    /**
     * Applies the scale to the transform matrix
     */
    protected void preRenderScale(EntityAptonoth par1, float par2)
    {
        GL11.glScalef(this.scale, this.scale, this.scale);
    }

    protected ResourceLocation func_110870_a(EntityAptonoth par1)
    {
        return texture;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
        this.preRenderScale((EntityAptonoth)par1EntityLivingBase, par2);
    }

    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110870_a((EntityAptonoth)par1Entity);
    }

	
}