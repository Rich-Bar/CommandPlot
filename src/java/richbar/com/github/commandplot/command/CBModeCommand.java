package richbar.com.github.commandplot.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.CommandBlockMode;

public class CBModeCommand implements CommandExecutor{

	CommandBlockMode cbMode;
	
	public CBModeCommand(CommandBlockMode cbMode) {
		this.cbMode = cbMode;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("true")) return disableMode(p);
				return enableMode(p);
			}else
				if(cbMode.isActive(p.getUniqueId())) return disableMode(p);
				return enableMode(p);
		}
		return false;
	}

	private boolean enableMode(Player p){
		p.setOp(true);
		return cbMode.addPlayer(p.getUniqueId());
	}
	
	private boolean disableMode(Player p){
		p.setOp(false);
		return cbMode.removePlayer(p.getUniqueId());
	}

}
