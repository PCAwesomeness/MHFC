package mhfc.heltrato.client.render.item;

import org.lwjgl.opengl.GL11;

import mhfc.heltrato.client.model.block.ModelStunTrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

public class RenderItemStunTrap implements IItemRenderer {
	
	private ModelStunTrap model;
	
	public RenderItemStunTrap(){
		model = new ModelStunTrap();
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
			renderThisItem(0.3F, 2.4F, 0.0F, 180F, 0F, 0F, 1F); break;
		case EQUIPPED:
			renderThisItem(0.6F, 3.5F, 0.8F, 180F, 0F, 0F, 1F); break;
		case EQUIPPED_FIRST_PERSON:
			renderThisItem(0.6F, 3.5F, 0.8F, 180F, 0F, 0F, 1F); break;
		case INVENTORY:
			renderThisItem(0.9F, 3.3F, 1.0F, 180F, 0F, 0F, 1F); break;
		default:
			break;
		}
		
		
		
	}
	
	private void renderThisItem(float x, float y, float z,  float rotateX, float rotateY, float rotateZ, float angle) {
		float scale = 0.1225F;
		Tessellator tess = Tessellator.instance;
		GL11.glPushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("mhfc:textures/tile/stuntrap.png"));
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(rotateX, rotateY, rotateZ, angle);
		model.renderModel(scale);
		GL11.glPopMatrix();
		
	}
}