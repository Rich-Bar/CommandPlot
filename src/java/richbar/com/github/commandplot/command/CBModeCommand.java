package richbar.com.github.commandplot.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
			if(command.getName().equalsIgnoreCase("commandblockmode")){
				if(args.length > 0){
					if(args[0].trim().equalsIgnoreCase("true")) return enableMode(p);
					return disableMode(p);
				}else
					if(cbMode.isActive(p.getUniqueId())) return disableMode(p);
					return enableMode(p);
			}
			if(command.getName().equalsIgnoreCase("commandblock")){
				p.getInventory().addItem(new ItemStack(Material.COMMAND, 1));
			}
		}
		return false;
	}

	private boolean enableMode(Player p){
		p.setOp(true);
		p.sendMessage("You've entered the 'Commanblock Mode', be aware that you cannot run any commands while in this mode!");
		return cbMode.addPlayer(p.getUniqueId());
	}
	
	private boolean disableMode(Player p){
		p.setOp(false);
		p.sendMessage("You've left the 'Commanblock Mode', you can run commands again now!");
		return cbMode.removePlayer(p.getUniqueId());
	}

}
