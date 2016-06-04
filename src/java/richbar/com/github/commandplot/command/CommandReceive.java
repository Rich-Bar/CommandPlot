package richbar.com.github.commandplot.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.minecraft.server.v1_9_R1.ICommand;
import net.minecraft.server.v1_9_R1.ICommandListener;

public class CommandReceive extends CustomCommand{

	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		
		
		return false;
	}

	@Override
	public String getCommand() {
		return "receive";
	}

	@Override
	public String getUsage(ICommandListener arg0) {
		return "[only Commandblocks!] use 'receive <x y z> [<x y z> ...]' to open this commandblock for 'transmit' command";
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

}
