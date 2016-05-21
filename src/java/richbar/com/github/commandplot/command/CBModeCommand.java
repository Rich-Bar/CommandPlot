package richbar.com.github.commandplot.command;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CBModeCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(sender instanceof Player){
			return true;
		}else
		return false;
	}


}
