package mhfc.net.common.util.gui;

import mhfc.net.MHFCMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

public class MHFCGuiUtil {

	public static final int COLOUR_FOREGROUND = 0xd8953c;
	public static final int COLOUR_TEXT = 0x404040;
	public static final int COLOUR_TITLE = 0x000000;

	private static final String WIDTH_WARNING = "The width of the draw was smaller than the first character. This creates a stack overflow.\n Please don't do this, track it down. Stacktrace:";
	public static float zLevel;
	private static ScaledResolution s;

	public static int realScreenWidth(Minecraft mc) {
		if (mc == null)
			throw new IllegalArgumentException(
				"Gui utils may only be accessed with valid minecraft");
		return mc.displayWidth;
	}

	public static int realScreenHeight(Minecraft mc) {
		if (mc == null)
			throw new IllegalArgumentException(
				"Gui utils may only be accessed with valid minecraft");
		return mc.displayHeight;
	}

	public static int minecraftWidth(Minecraft mc) {
		refreshScaled(mc);
		return s.getScaledWidth();
	}

	public static int minecraftHeight(Minecraft mc) {
		refreshScaled(mc);
		return s.getScaledHeight();
	}

	public static int guiScaleFactor(Minecraft mc) {
		refreshScaled(mc);
		return s.getScaleFactor();
	}

	private static void refreshScaled(Minecraft mc) {
		if (mc == null)
			throw new IllegalArgumentException(
				"Gui utils may only be accessed with valid minecraft");
		s = new ScaledResolution(mc.gameSettings, mc.displayWidth,
			mc.displayHeight);
	}

	/**
	 * Draws a string onto the screen at the desired position. If width is > 0,
	 * then the draw split string method is used instead. The amount if vertical
	 * space occupied by the draw is calculated and returned. If one attempts to
	 * draw a null String or with a null renderer, a warning is printed
	 * (including a stack trace) and 0 is returned.
	 * 
	 * @return The drawn height of the string. Always line height for valid
	 *         parameters and width==0
	 */
	public static int drawTextAndReturnHeight(FontRenderer fRend,
		String string, int posX, int posY, int width, int colour) {
		if (fRend == null || string == null) {
			MHFCMain.logger.warn(fRend == null
				? "Null renderer used as argument"
				: "Render request for a null string");
			Thread.dumpStack();
			return 0;
		}
		int lines = 1;
		if (width <= 0) {
			fRend.drawString(string, posX, posY, colour);
		} else if (isDrawWidthTooSmall(fRend, width, string)) {
			MHFCMain.logger.info(WIDTH_WARNING);
			Thread.dumpStack();
			lines = 0;
		} else {
			lines = fRend.listFormattedStringToWidth(string, width).size();
			fRend.drawSplitString(string, posX, posY, width, colour);
		}
		return lines * fRend.FONT_HEIGHT;
	}

	/**
	 * Considers if the draw width would cause Minecraft to crash using the
	 * given string. This can happen when the first character can't even fit
	 * into the width.
	 * 
	 * @return If the font renderer will crash the game with this string and
	 *         this width
	 */
	public static boolean isDrawWidthTooSmall(FontRenderer fRend, int width,
		String string) {
		return !string.isEmpty()
			&& width < fRend.getStringWidth(string.substring(0, 1));
	}

	public static void drawTexturedRectangle(double xMin, double yMin,
		double width, double height, float u, float v, float uWidth,
		float vHeight) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertexWithUV(xMin, yMin, zLevel, u, v);
		t.addVertexWithUV(xMin, yMin + height, zLevel, u, v + vHeight);
		t.addVertexWithUV(xMin + width, yMin + height, zLevel, u + uWidth, v
			+ vHeight);
		t.addVertexWithUV(xMin + width, yMin, zLevel, u + uWidth, v);
		t.draw();
	}

	public static void drawTexturedBoxFromBorder(int x, int y, int width,
		int height) {
		drawTexturedBoxFromBorder(x, y, zLevel, width, height);
	}

	public static void drawTexturedBoxFromBorder(int x, int y, float zLevel,
		int width, int height) {
		drawTexturedBoxFromBorder(x, y, zLevel, width, height, Math.min(Math
			.min(15, width / 2), height / 2));
	}

	public static void drawTexturedBoxFromBorder(int x, int y, float zLevel,
		int width, int height, int borderSize) {
		drawTexturedBoxFromBorder(x, y, zLevel, width, height, borderSize,
			borderSize / 256f);
	}

	public static void drawTexturedBoxFromBorder(int x, int y, float zLevel,
		int width, int height, int borderSize, float borderUV) {
		drawTexturedBoxFromBorder(x, y, zLevel, width, height, borderSize,
			borderUV, 1, 1);
	}

	public static void drawTexturedBoxFromBorder(int x, int y, float zLevel,
		int width, int height, int borderSize, float borderUV, float maxU,
		float maxV) {
		drawTexturedBoxFromBorder(x, y, zLevel, width, height, borderSize,
			borderUV, borderUV, maxU, maxV);
	}

	public static void drawTexturedBoxFromBorder(int x, int y, float zLevel,
		int width, int height, int borderSize, float borderU, float borderV,
		float maxU, float maxV) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y, zLevel, 0, 0);
		tess.addVertexWithUV(x, y + borderSize, zLevel, 0, borderV);
		tess.addVertexWithUV(x + borderSize, y + borderSize, zLevel, borderU,
			borderV);
		tess.addVertexWithUV(x + borderSize, y, zLevel, borderU, 0);
		tess.draw();
		tess.startDrawingQuads();
		tess.addTranslation(width - borderSize, 0, 0);
		tess.addVertexWithUV(x, y, zLevel, maxU - borderU, 0);
		tess.addVertexWithUV(x, y + borderSize, zLevel, maxU - borderU, borderV);
		tess.addVertexWithUV(x + borderSize, y + borderSize, zLevel, maxU,
			borderV);
		tess.addVertexWithUV(x + borderSize, y, zLevel, maxU, 0);
		tess.draw();
		tess.startDrawingQuads();
		tess.addTranslation(0, height - borderSize, 0);
		tess.addVertexWithUV(x, y, zLevel, maxU - borderU, maxV - borderV);
		tess.addVertexWithUV(x, y + borderSize, zLevel, maxU - borderU, maxV);
		tess.addVertexWithUV(x + borderSize, y + borderSize, zLevel, maxU, maxV);
		tess.addVertexWithUV(x + borderSize, y, zLevel, maxU, maxV - borderV);
		tess.draw();
		tess.startDrawingQuads();
		tess.addTranslation(-width + borderSize, 0, 0);
		tess.addVertexWithUV(x, y, zLevel, 0, maxV - borderV);
		tess.addVertexWithUV(x, y + borderSize, zLevel, 0, maxV);
		tess.addVertexWithUV(x + borderSize, y + borderSize, zLevel, borderU,
			maxV);
		tess.addVertexWithUV(x + borderSize, y, zLevel, borderU, maxV - borderV);
		tess.draw();
		tess.addTranslation(0, -height + borderSize, 0);

		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y + borderSize, zLevel, 0, borderV);
		tess.addVertexWithUV(x, y + height - borderSize, zLevel, 0, maxV
			- borderV);
		tess.addVertexWithUV(x + borderSize, y + height - borderSize, zLevel,
			borderU, maxV - borderV);
		tess.addVertexWithUV(x + borderSize, y + borderSize, zLevel, borderU,
			borderV);
		tess.draw();

		tess.startDrawingQuads();
		tess.addVertexWithUV(x + width - borderSize, y + borderSize, zLevel,
			maxU - borderU, borderV);
		tess.addVertexWithUV(x + width - borderSize, y + height - borderSize,
			zLevel, maxU - borderU, maxV - borderV);
		tess.addVertexWithUV(x + width, y + height - borderSize, zLevel, maxU,
			maxV - borderV);
		tess.addVertexWithUV(x + width, y + borderSize, zLevel, maxU, borderV);
		tess.draw();

		tess.startDrawingQuads();
		tess.addVertexWithUV(x + borderSize, y, zLevel, borderU, 0);
		tess.addVertexWithUV(x + borderSize, y + borderSize, zLevel, borderU,
			borderV);
		tess.addVertexWithUV(x + width - borderSize, y + borderSize, zLevel,
			maxU - borderU, borderV);
		tess.addVertexWithUV(x + width - borderSize, y, zLevel, maxU - borderU,
			0);
		tess.draw();

		tess.startDrawingQuads();
		tess.addVertexWithUV(x + borderSize, y + height - borderSize, zLevel,
			borderU, maxV - borderV);
		tess.addVertexWithUV(x + borderSize, y + height, zLevel, borderU, maxV);
		tess.addVertexWithUV(x + width - borderSize, y + height, zLevel, maxU
			- borderU, maxV);
		tess.addVertexWithUV(x + width - borderSize, y + height - borderSize,
			zLevel, maxU - borderU, maxV - borderV);
		tess.draw();

		tess.startDrawingQuads();
		tess.addVertexWithUV(x + borderSize, y + borderSize, zLevel, borderU,
			borderV);
		tess.addVertexWithUV(x + borderSize, y + height - borderSize, zLevel,
			borderU, maxV - borderV);
		tess.addVertexWithUV(x + width - borderSize, y + height - borderSize,
			zLevel, maxU - borderU, maxV - borderV);
		tess.addVertexWithUV(x + width - borderSize, y + borderSize, zLevel,
			maxU - borderU, borderV);
		tess.draw();

	}

}
