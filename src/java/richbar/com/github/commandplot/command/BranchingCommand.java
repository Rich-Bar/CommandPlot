package richbar.com.github.commandplot.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Java15Compat;

public class BranchingCommand implements CommandExecutor{

	Map<String, CommandExecutor> subExecutors = new HashMap<>(); 
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(args.length == 0) return false;
		args[0] = args[0].toLowerCase();
		if(subExecutors.containsKey(args[0]))
			return subExecutors.get(args[0]).onCommand(sender, command, args[0], Java15Compat.Arrays_copyOfRange(args, 1, args.length -1));
		return false;
	}

}
