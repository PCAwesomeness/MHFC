package mhfc.heltrato.client.render.item;

import org.lwjgl.opengl.GL11;

import mhfc.heltrato.client.model.block.ModelBBQSpit;
import mhfc.heltrato.client.model.block.ModelHunterBench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class RenderItemBBQSpit implements IItemRenderer {
	
	private ModelBBQSpit model;
	
	public RenderItemBBQSpit() {
		model = new ModelBBQSpit();
	}

	
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		case ENTITY:
			renderThisItem(0.3F, 1.1F, 0.0F, 180F, 0F, 0F, 1F); break;
		case EQUIPPED:
			renderThisItem(0.6F, 2F, 0.8F, 180F, 0F, 0F, 1F); break;
		case EQUIPPED_FIRST_PERSON:
			renderThisItem(0.6F, 2F, 0.8F, 180F, 0F, 0F, 1F); break;
		case INVENTORY:
			renderThisItem(0.9F, 2F, 1.0F, 180F, 0F, 0F, 1F); break;
		default:
			break;
		}
		
		
		
	}
	
	private void renderThisItem(float x, float y, float z, float rotateX, float rotateY, float rotateZ, float angle) {
		float scale = 0.07125F;
		Tessellator tess = Tessellator.instance;
		GL11.glPushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("mhfc:textures/tile/bbqspit.png"));
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(rotateX, rotateY, rotateZ, angle);
		model.renderModel(scale);
		GL11.glPopMatrix();
		
	}

}