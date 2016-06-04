package richbar.com.github.commandplot.command;

import org.bukkit.command.CommandExecutor;

import net.minecraft.server.v1_9_R1.CommandAbstract;
import net.minecraft.server.v1_9_R1.CommandException;
import net.minecraft.server.v1_9_R1.ICommand;
import net.minecraft.server.v1_9_R1.ICommandListener;
import net.minecraft.server.v1_9_R1.MinecraftServer;

public abstract class CustomCommand extends CommandAbstract implements CommandExecutor{

	
	@Override
	public void execute(MinecraftServer paramMinecraftServer,
			ICommandListener paramICommandListener, String[] paramArrayOfString)
			throws CommandException {}
	
	@Override
	public int compareTo(ICommand o) {
		return 0;
	}
}
