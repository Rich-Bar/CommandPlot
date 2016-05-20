package richbar.com.github.commandplot.command;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;

import net.minecraft.server.v1_9_R1.CommandAbstract;
import net.minecraft.server.v1_9_R1.CommandException;
import net.minecraft.server.v1_9_R1.ICommand;
import net.minecraft.server.v1_9_R1.ICommandListener;
import net.minecraft.server.v1_9_R1.MinecraftServer;

public class TransmitCommand extends CommandAbstract{

	@Override
	public void execute(MinecraftServer server, ICommandListener listener, String[] command) throws CommandException {

	}

	@Override
	public String getCommand() {
		return "transmit";
	}

	@Override
	public String getUsage(ICommandListener arg0) {
		return "[only Commandblocks!] use 'transmit <x y z>' to trigger the commandblock at pos <x y z>. The Command block to receive needs to specify your blocks pos!";
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

}
