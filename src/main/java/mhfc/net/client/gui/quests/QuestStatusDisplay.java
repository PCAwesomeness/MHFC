package mhfc.net.client.gui.quests;

import mhfc.net.client.quests.MHFCRegQuestVisual;
import mhfc.net.common.quests.QuestRunningInformation;
import mhfc.net.common.util.gui.MHFCGuiUtil;
import mhfc.net.common.util.lib.MHFCReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import static mhfc.net.common.util.gui.MHFCGuiUtil.*;

public class QuestStatusDisplay extends Gui {

	private int[] heightFromScale = {166, 166, 166, 166};
	private Minecraft mc = Minecraft.getMinecraft();
	private int displayTick;

	public QuestStatusDisplay() {
		super();
		displayTick = 0;
	}

	@SubscribeEvent
	public void onDrawOfGUI(DrawScreenEvent.Pre screenEvent) {
		QuestRunningInformation information = MHFCRegQuestVisual
			.getPlayerVisual();
		if (screenEvent.gui instanceof GuiInventory) {
			mc.getTextureManager().bindTexture(
				MHFCRegQuestVisual.QUEST_STATUS_INVENTORY_BACKGROUND);
			int scale = mc.gameSettings.guiScale & 3;
			if (scale == 0) {
				scale = 3;
			}
			int width = 200;
			int height = heightFromScale[scale];
			// Tessellator.instance.addTranslation(0f, 0f, 0.5f);
			int scaledWidth = MHFCGuiUtil.minecraftWidth(mc);
			int scaledHeight = MHFCGuiUtil.minecraftHeight(mc);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			int positionX = (scaledWidth) / 2 + 96;
			int positionY = (scaledHeight - height) / 2;
			width = Math.min(width, scaledWidth - positionX - 10);
			GL11.glTranslatef(0, 0, 0.5f);
			MHFCGuiUtil.drawTexturedBoxFromBorder(positionX, positionY,
				this.zLevel, width, height, 40, 40f / 256, 248f / 256,
				166f / 256);

			if (information == null) {
				String drawn = "No quest accepted";
				int stringPosY = positionY
					+ (height - mc.fontRenderer.FONT_HEIGHT) / 2, stringPosX = positionX
					+ (width - mc.fontRenderer.getStringWidth(drawn)) / 2;
				mc.fontRenderer.drawString(drawn, stringPosX, stringPosY,
					COLOUR_TITLE);
			} else {
				if (!isMouseOverInfo(positionX, positionY, width, height)) {
					displayTick++;
				}
				information.drawInformation(positionX, positionY, width,
					height, mc.fontRenderer, displayTick);
			}
			GL11.glTranslatef(0, 0, -0.5f);
		}
	}

	private boolean isMouseOverInfo(int positionX, int positionY, int width,
		int height) {
		int i = MHFCGuiUtil.minecraftWidth(mc);
		int j = MHFCGuiUtil.minecraftWidth(mc);
		final int k = Mouse.getX() * i / this.mc.displayWidth;
		final int l = j - Mouse.getY() * j / this.mc.displayHeight - 1;
		return (k > positionX && k < positionX + width && l > positionY && l < positionY
			+ height);
	}

	@SubscribeEvent
	public void onDraw(RenderGameOverlayEvent.Post overlayEvent) {
		QuestRunningInformation information = MHFCRegQuestVisual
			.getPlayerVisual();
		if (overlayEvent.type == ElementType.FOOD && information != null) {
			mc.getTextureManager().bindTexture(
				MHFCRegQuestVisual.QUEST_STATUS_ONSCREEN_BACKGROUND);
			GL11.glEnable(GL11.GL_BLEND);
			int width = 85;
			int height = 200;
			GL14.glBlendColor(0.0f, 0.0f, 0.0f, 0.6f);
			GL11.glBlendFunc(GL11.GL_CONSTANT_ALPHA,
				GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
			int posX = MHFCGuiUtil.minecraftWidth(mc) - width, posY = (MHFCGuiUtil
				.minecraftHeight(mc) - height) / 2;
			MHFCGuiUtil.drawTexturedBoxFromBorder(posX, posY, this.zLevel,
				width, height, 40, 30f / 256, 248f / 256, 166f / 256);
			GL11.glDisable(GL11.GL_BLEND);
			String localizedStat = StatCollector
				.translateToLocal(MHFCReference.unlocalized_tag_status_short);
			mc.fontRenderer.drawString(localizedStat, posX + 5, posY + 5,
				0x804040);
			int lineHeight = mc.fontRenderer.FONT_HEIGHT + 2;
			mc.fontRenderer.drawSplitString(information.getShortStatus(),
				posX + 5, posY + lineHeight + 5, width - 10, COLOUR_TEXT);
		}
	}
}
