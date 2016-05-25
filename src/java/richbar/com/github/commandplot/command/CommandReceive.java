package richbar.com.github.commandplot.command;

import net.minecraft.server.v1_9_R1.CommandAbstract;
import net.minecraft.server.v1_9_R1.CommandException;
import net.minecraft.server.v1_9_R1.ICommand;
import net.minecraft.server.v1_9_R1.ICommandListener;
import net.minecraft.server.v1_9_R1.MinecraftServer;

public class CommandReceive extends CommandAbstract{

	@Override
	public void execute(MinecraftServer server, ICommandListener listener, String[] command) throws CommandException {
		
	}

	@Override
	public String getCommand() {
		return "receive";
	}

	@Override
	public String getUsage(ICommandListener arg0) {
		return "[only Commandblocks!] use 'receive <x y z> <x y z>' to open this commandblock for 'transmit' command";
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

}
