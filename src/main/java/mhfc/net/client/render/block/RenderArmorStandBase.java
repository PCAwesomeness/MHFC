package mhfc.net.client.render.block;

import mhfc.net.client.model.block.ModelArmorStandBase;
import mhfc.net.common.util.lib.MHFCReference;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderArmorStandBase extends TileEntitySpecialRenderer {

	private ModelArmorStandBase model;
	public RenderArmorStandBase() {
		model = new ModelArmorStandBase();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y,
			double z, float f) {
		GL11.glPushMatrix();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) x + 0.5F, (float) y - -1.5F, (float) z + 0.5F);
		GL11.glRotatef(180F, 0F, 0F, 1F);
		bindTexture(new ResourceLocation(MHFCReference.tile_armorstandbase_tex));
		GL11.glPushMatrix();
		model.renderModel(0.0625F);
		GL11.glPopMatrix();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

}