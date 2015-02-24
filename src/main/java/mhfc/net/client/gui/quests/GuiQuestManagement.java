package mhfc.net.client.gui.quests;

import java.util.List;

import mhfc.net.client.gui.IMHFCTab;
import mhfc.net.client.quests.MHFCRegQuestVisual;
import mhfc.net.common.network.PacketPipeline;
import mhfc.net.common.network.packet.MessageQuestInteraction;
import mhfc.net.common.network.packet.MessageQuestInteraction.Interaction;
import mhfc.net.common.util.gui.MHFCGuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class GuiQuestManagement extends GuiScreen implements IMHFCTab {

	private static final int startQuestHeight = 20;
	private static final int startQuestWidth = 120;
	private List<Slot> chestSlots;
	private int xPos, yPos;
	private int xSize, ySize;
	// TODO Change functionality of start button to a switch to vote
	private GuiButton cancelQuest, startQuest;
	private final EntityPlayer accessor;

	public GuiQuestManagement(List<Slot> chestSlots, EntityPlayer player) {
		this.accessor = player;
		this.chestSlots = chestSlots;
		this.xSize = 374;
		this.ySize = 220;
		cancelQuest = new GuiButton(0, 25, 10, 120, 20, "Cancel current Quest") {
			@Override
			public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_,
					int p_146116_3_) {
				if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_)) {
					PacketPipeline.networkPipe
							.sendToServer(new MessageQuestInteraction(
									Interaction.GIVE_UP, new String[0]));
					return true;
				}
				return false;
			}
		};
		startQuest = new GuiButton(0, 25, 10, startQuestWidth,
				startQuestHeight, "Start current Quest") {
			@Override
			public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_,
					int p_146116_3_) {
				if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_)) {
					PacketPipeline.networkPipe
							.sendToServer(new MessageQuestInteraction(
									Interaction.VOTE_START, new String[0]));
					accessor.closeScreen();
					return true;
				}
				return false;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.add(cancelQuest);
		buttonList.add(startQuest);
		super.initGui();
	}

	@Override
	public void drawBackground(int p_146278_1_) {
		mc.getTextureManager().bindTexture(
				MHFCRegQuestVisual.QUEST_BOARD_BACKGROUND);
		MHFCGuiUtil.drawTexturedBoxFromBorder(xPos, yPos, this.zLevel,
				this.xSize, this.ySize, 0, 0, 1f, 1f);
		super.drawBackground(p_146278_1_);
	}

	@Override
	public void drawTab(int posX, int posY, int mousePosX, int mousePosY,
			float partialTick) {
		this.xPos = posX;
		this.yPos = posY;
		updateScreen();
		drawScreen(mousePosX, mousePosY, partialTick);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		String warning = "You are already on a quest";
		int warnX = (xSize - fontRendererObj.getStringWidth(warning)) / 2, warnY = 60;
		fontRendererObj.drawString(warning, warnX + xPos, warnY + yPos,
				0x404040);
		super.drawScreen(mouseX, mouseY, partialTick);
	}

	@Override
	public void handleClick(int relativeX, int relativeY, int button) {
		super.mouseClicked(relativeX, relativeY, button);
	}

	@Override
	public boolean containsSlot(Slot slot) {
		if (slot.inventory instanceof InventoryPlayer)
			return true;
		return chestSlots == null ? false : chestSlots.contains(slot);
	}

	@Override
	public void updateTab(int posX, int posY) {
		cancelQuest.xPosition = xPos + xSize / 2 - cancelQuest.getButtonWidth()
				/ 2;
		cancelQuest.yPosition = yPos + ySize / 2 + 5;
		startQuest.xPosition = xPos + xSize / 2 - startQuest.getButtonWidth()
				/ 2;
		startQuest.yPosition = yPos + ySize / 2 - startQuestHeight - 10;
	}

	@Override
	public void updateScreen() {
		xPos = (width - xSize) / 2;
		yPos = (height - ySize) / 2;
		updateTab(xPos, yPos);
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMovementMouseDown(int mouseX, int mouseY, int button,
			long timeDiff) {
	}

	@Override
	public void handleMouseUp(int mouseX, int mouseY, int id) {
		super.mouseMovedOrUp(mouseX, mouseY, id);
	}

	@Override
	public void handleMovement(int mouseX, int mouseY) {
	}
}