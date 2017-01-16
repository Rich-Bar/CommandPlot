package richbar.com.github.commandplot;

import net.minecraft.server.v1_10_R1.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.craftbukkit.v1_10_R1.command.VanillaCommandWrapper;
import richbar.com.github.commandplot.util.Util;

/**
 * Created by Rich Y on 02.01.2017.
 */
class CommandWrapper extends VanillaCommand {
	
	private final CommandAbstract backend;
	
	protected CommandWrapper(String name, CommandAbstract backend) {
		super(name);
		this.backend = backend;
	}
	
	/***
	 * @see VanillaCommandWrapper
	 * @param sender
	 * @param label
	 * @param args
	 * @return success
	 */
	@Override
	public boolean execute(CommandSender sender, String label, String... args) {
		if(CPlugin.isDebug) System.out.println("Wrapping Command: " + label + " " + String.join(" ", args) + " - exec by " + sender.getName());
		ICommandListener commandListener = Util.getListener(sender);
		WorldServer[] worldServer = MinecraftServer.getServer().worldServer;
		MinecraftServer server = MinecraftServer.getServer();
		server.worldServer = new WorldServer[server.worlds.size()];
		server.worldServer[0] = (WorldServer) commandListener.getWorld();
		int i = 0;
		
		for (int throwable = 1; throwable < server.worldServer.length; ++throwable) {
			WorldServer chatmessage3 = server.worlds.get(i++);
			if (server.worldServer[0] == chatmessage3) {
				--throwable;
			} else {
				server.worldServer[throwable] = chatmessage3;
			}
		}
		try {
			backend.execute(server, commandListener, args);
			MinecraftServer.getServer().worldServer = worldServer;
			return true;
		} catch (CommandException ignored) {
		}
		MinecraftServer.getServer().worldServer = worldServer;
		return false;
	}
}
