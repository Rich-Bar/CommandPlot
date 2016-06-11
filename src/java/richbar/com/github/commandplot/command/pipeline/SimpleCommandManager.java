package richbar.com.github.commandplot.command.pipeline;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.minecart.CommandMinecart;

public class SimpleCommandManager extends Command{

	Command next;
	
	protected SimpleCommandManager(Command c) {
		super(c.getName(), c.getDescription(), c.getUsage(), c.getAliases());
		this.next = c;
	}

	public Command getNext(){
		return next;
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(sender instanceof BlockCommandSender || sender instanceof CommandMinecart) 
			return false;
		else 
			return next.execute(sender, label, args);
	}

}
