package mhfc.net.common.core.registry;

import java.util.*;

import mhfc.net.MHFCMain;
import mhfc.net.common.eventhandler.MHFCInteractionHandler.MHFCInteractionEvent;
import mhfc.net.common.network.PacketPipeline;
import mhfc.net.common.network.packet.*;
import mhfc.net.common.quests.GeneralQuest;
import mhfc.net.common.quests.QuestRunningInformation;
import mhfc.net.common.quests.QuestVisualInformation;
import mhfc.net.common.quests.api.QuestDescription;
import mhfc.net.common.quests.api.QuestFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MHFCQuestRegistry {

	public static class RegistryRequestVisualHandler
		implements
			IMessageHandler<MessageRequestQuestVisual, MessageQuestVisual> {

		@Override
		public MessageQuestVisual onMessage(MessageRequestQuestVisual message,
			MessageContext ctx) {
			String identifier = message.getIdentifier();
			QuestDescription description = MHFCQuestBuildRegistry
				.getQuestDescription(identifier);
			QuestVisualInformation info = (description == null
				? QuestVisualInformation.IDENTIFIER_ERROR
				: description.getVisualInformation());
			String[] stringArray = {identifier, info.getName(),
					info.getDescription(), info.getClient(), info.getAims(),
					info.getFails(), info.getAreaID(),
					info.getTimeLimitAsString(), info.getType().getAsString(),
					info.getRewardString(), info.getFeeString(),
					info.getMaxPartySize()};
			return new MessageQuestVisual(stringArray);
		}
	}

	public static class RunningSubscriptionHandler
		implements
			IMessageHandler<MessageQuestRunningSubscription, IMessage> {
		private static Set<EntityPlayerMP> subscribers = new HashSet<EntityPlayerMP>();

		public RunningSubscriptionHandler() {
		}

		@Override
		public IMessage onMessage(MessageQuestRunningSubscription message,
			MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			if (message.isSubscribed()) {
				subscribers.add(player);
				sendAllTo(player);
			} else {
				subscribers.remove(player);
			}
			return null;
		}

		public static void sendAllTo(EntityPlayerMP player) {
			for (GeneralQuest q : questsRunning) {
				String identifier = runningQuestToStringMap.get(q);
				MessageQuestVisual messageSent = new MessageQuestVisual(
					identifier, q.getRunningInformation());
				messageSent.setTypeID(2);
				PacketPipeline.networkPipe.sendTo(messageSent, player);
			}
		}

		public static void sendToAll(MessageQuestVisual visual) {
			Iterator<EntityPlayerMP> iter = subscribers.iterator();
			while (iter.hasNext()) {
				PacketPipeline.networkPipe.sendTo(visual, iter.next());
			}
		}
	}

	public static class PlayerConnectionHandler {

		@SubscribeEvent
		public void onPlayerJoin(PlayerLoggedInEvent logIn) {
			EntityPlayerMP playerMP = (EntityPlayerMP) logIn.player;
			PacketPipeline.networkPipe.sendTo(new MessageQuestScreenInit(
				MHFCQuestBuildRegistry.groupMapping,
				MHFCQuestBuildRegistry.groupIDs), playerMP);
			RunningSubscriptionHandler.subscribers.add(playerMP);
			RunningSubscriptionHandler.sendAllTo(playerMP);
		}

		@SubscribeEvent
		public void onPlayerLeave(PlayerLoggedOutEvent logOut) {
			GeneralQuest q = playerQuest.get(logOut.player);
			if (q == null)
				return;
			q.removePlayer(logOut.player);
			RunningSubscriptionHandler.subscribers.remove(logOut.player);
		}
	}

	@SubscribeEvent
	void onPlayerInteraction(MHFCInteractionEvent event) {
		GeneralQuest quest;
		EntityPlayerMP player = event.player;
		MessageMHFCInteraction message = event.message;
		switch (event.interaction) {
			case NEW_QUEST :
				quest = getQuestForPlayer(player);
				if (quest == null) {
					String registerFor = message.getOptions()[0] + "@"
						+ player.getDisplayName();
					GeneralQuest newQuest = QuestFactory.constructQuest(
						MHFCQuestBuildRegistry.getQuestDescription(message
							.getOptions()[0]), player, registerFor);
					if (newQuest == null) {
						player.addChatMessage(new ChatComponentText(
							"Quest not found"));
						return;
					}
				} else {
					player.addChatMessage(new ChatComponentText(
						"You already are on quest "
							+ getIdentifierForQuest(quest)));
					String id = getIdentifierForQuest(quest);
					PacketPipeline.networkPipe.sendTo(
						new<QuestRunningInformation> MessageQuestVisual(id,
							quest.getRunningInformation()), player);
				}
				break;
			case ACCEPT_QUEST :
				quest = getRunningQuest(message.getOptions()[0]);
				if (quest != null) {
					quest.addPlayer(player);
				}
				break;
			case START_QUEST :
				quest = getQuestForPlayer(player);
				if (quest != null) {
					quest.voteStart(player);
				}
				break;
			case END_QUEST :
				quest = getQuestForPlayer(player);
				if (quest != null) {
					quest.voteEnd(player);
				} else {
					PacketPipeline.networkPipe.sendTo(new MessageQuestVisual(
						"", null), player);
				}
				break;
			case FORFEIT_QUEST :
				quest = getQuestForPlayer(player);
				if (quest != null) {
					quest.removePlayer(player);
				} else {
					PacketPipeline.networkPipe.sendTo(new MessageQuestVisual(
						"", null), player);
				}
				break;
			case MOD_RELOAD :
				for (GeneralQuest genQ : questsRunning) {
					RunningSubscriptionHandler
						.sendToAll(new MessageQuestVisual(
							getIdentifierForQuest(genQ), genQ
								.getRunningInformation()));
				}
				break;
			default :
				break;
		}
	}

	protected static HashMap<EntityPlayer, GeneralQuest> playerQuest = new HashMap<EntityPlayer, GeneralQuest>();
	protected static List<GeneralQuest> questsRunning = new ArrayList<GeneralQuest>();
	protected static Map<String, GeneralQuest> runningQuestFromStringMap = new HashMap<String, GeneralQuest>();
	protected static Map<GeneralQuest, String> runningQuestToStringMap = new HashMap<GeneralQuest, String>();

	public static void init() {
		FMLCommonHandler.instance().bus().register(new MHFCQuestRegistry());
		FMLCommonHandler.instance().bus().register(
			new PlayerConnectionHandler());
	}

	public static GeneralQuest getRunningQuest(String string) {
		return runningQuestFromStringMap.get(string);
	}

	/**
	 * Get the quest on which a player is on. If the player is on no quest then
	 * null is returned.
	 */
	public static GeneralQuest getQuestForPlayer(EntityPlayer player) {
		return playerQuest.get(player);
	}

	/**
	 * Returns all quests that are running at the moment.
	 */
	public static List<GeneralQuest> getRunningQuests() {
		return questsRunning;
	}

	/**
	 * Returns the identifier for a quest
	 */
	public static String getIdentifierForQuest(GeneralQuest quest) {
		return runningQuestToStringMap.get(quest);
	}

	/**
	 * Sets the quest for a player, use null to remove the entry
	 *
	 */
	public static void setQuestForPlayer(EntityPlayer player,
		GeneralQuest generalQuest) {
		if (generalQuest == null)
			playerQuest.remove(player);
		else
			playerQuest.put(player, generalQuest);
	}

	/**
	 * Should be called when a new quest was started
	 */
	public static void regRunningQuest(GeneralQuest generalQuest,
		String identifier) {
		questsRunning.add(generalQuest);
		MHFCMain.logger.info(questsRunning.size()
			+ " quests are running at the moment.");
		runningQuestFromStringMap.put(identifier, generalQuest);
		runningQuestToStringMap.put(generalQuest, identifier);
		MessageQuestVisual message = new MessageQuestVisual(identifier,
			generalQuest.getRunningInformation());
		message.setTypeID(2);
		RunningSubscriptionHandler.sendToAll(message);
	}

	/**
	 * Should be called when a quest is no longer running and was terminated.
	 * Might also be called to ensure client sync. Returns true if the quest was
	 * registered.
	 */
	public static boolean deregRunningQuest(GeneralQuest generalQuest) {
		boolean wasRunning = questsRunning.remove(generalQuest);
		String key = runningQuestToStringMap.remove(generalQuest);
		runningQuestFromStringMap.remove(key);
		MessageQuestVisual message = new<QuestRunningInformation> MessageQuestVisual(
			key, null);
		message.setTypeID(2);
		RunningSubscriptionHandler.sendToAll(message);
		return wasRunning;
	}

	/*
	 * Should be called when major changes to a quest were made that require
	 * resending the information to all clients
	 */
	public static void questUpdated(GeneralQuest q) {
		String identifier = runningQuestToStringMap.get(q);
		if (q == null || identifier == null)
			return;
		MessageQuestVisual message = new MessageQuestVisual(identifier, q
			.getRunningInformation());
		message.setTypeID(2);
		RunningSubscriptionHandler.sendToAll(message);
	}
}
