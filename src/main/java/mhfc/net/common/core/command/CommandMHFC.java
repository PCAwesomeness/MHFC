package mhfc.net.common.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mhfc.net.common.core.registry.MHFCRegQuests;
import mhfc.net.common.network.packet.MessageQuestInteraction;
import mhfc.net.common.network.packet.MessageQuestInteraction.Interaction;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandMHFC implements ICommand {

	List<String> aliases;

	public CommandMHFC() {
		aliases = new ArrayList<String>();
		aliases.add("mhfc");
		aliases.add("MHFC");
	}

	@Override
	public int compareTo(Object o) {
		return -1;
	}

	@Override
	public String getCommandName() {
		return "Mhfc";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/mhfc <action> [options]";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] parameters) {
		if (sender instanceof EntityPlayer) {
			Interaction action;
			if (parameters.length == 0)
				return;
			switch (parameters[0]) {
				case "new" :
					action = Interaction.START_NEW;
					break;
				case "surrender" :
					action = Interaction.VOTE_END;
					break;
				case "accept" :
					action = Interaction.ACCEPT;
					break;
				case "leave" :
					action = Interaction.GIVE_UP;
					break;
				case "start" :
					action = Interaction.VOTE_START;
					break;
				case "reload" :
					action = Interaction.MOD_RELOAD;
				default :
					return;
			}
			MHFCRegQuests.pipeline.networkPipe
					.sendToServer(new MessageQuestInteraction(action,
							(EntityPlayer) sender, Arrays.copyOfRange(
									parameters, 1, parameters.length)));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] options) {
		List<String> list = new ArrayList<String>();
		if (options.length == 1) {
			for (String s : new String[]{"accept", "leave", "new", "surrender",
					"start"}) {
				if (options[0] == null || s.startsWith(options[0]))
					list.add(s);
			}
		}
		return list;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
