package richbar.com.github.commandplot.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import richbar.com.github.commandplot.CPlugin;
import richbar.com.github.commandplot.backends.CommandBlockMode;
import richbar.com.github.commandplot.caching.objects.UUIDObject;

public class CBModeCommand implements CommandExecutor{

	private final CommandBlockMode cbMode;
	private final CPlugin main;
	
	
	public CBModeCommand(CPlugin main, CommandBlockMode cbMode) {
		this.main = main;
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
					if(cbMode.contains(new UUIDObject(p.getUniqueId()))) return disableMode(p);
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
		p.sendMessage(main.messages.getString("cbm-enter"));
		return cbMode.addObject(new UUIDObject(p.getUniqueId()));
	}
	
	private boolean disableMode(Player p){
		p.setOp(false);
		p.sendMessage(main.messages.getString("cbm-leave"));
		return cbMode.remove(new UUIDObject(p.getUniqueId()));
	}

}
