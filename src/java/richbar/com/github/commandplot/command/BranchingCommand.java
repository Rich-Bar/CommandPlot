package richbar.com.github.commandplot.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class BranchingCommand implements CommandExecutor{

	protected Map<String, CommandExecutor> subExecutors = new HashMap<>(); 
	public FileConfiguration messages;
	
	public BranchingCommand(FileConfiguration messages) {
		this.messages = messages;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(args.length == 0) return false;
		args[0] = args[0].toLowerCase();
		if(subExecutors.containsKey(args[0]))
			return subExecutors.get(args[0]).onCommand(sender, command, args[0], Arrays.copyOfRange(args, 1, args.length));
		sender.sendMessage(messages.getString("subcommands") + " " + String.join(", ", subExecutors.keySet()));
		return false;
	}

}
