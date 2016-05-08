
package richbar.com.github.plotfix;

import com.google.common.base.Joiner;

import net.minecraft.server.v1_9_R1.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_9_R1.command.VanillaCommandWrapper;

public abstract class FixCommandBlockListener extends CommandBlockListenerAbstract{
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private int sucessCount;
	private boolean chatOutput = true;
	private IChatBaseComponent chat = null;
	private String command = "";
	private String name = "@";
	private final CommandObjectiveExecutor executor = new CommandObjectiveExecutor();
	protected CommandSender sender;

	public void a(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("Command", this.command);
		nbttagcompound.setInt("SuccessCount", this.sucessCount);
		nbttagcompound.setString("CustomName", this.name);
		nbttagcompound.setBoolean("TrackOutput", this.chatOutput);
		if ((this.chat != null) && (this.chatOutput)) {
			nbttagcompound.setString("LastOutput",
					IChatBaseComponent.ChatSerializer.a(this.chat));
		}

		this.executor.b(nbttagcompound);
	}

	public String getCommand() {
		return this.command;
	}

	public void a(World world) {
		if (world.isClientSide) {
			this.sucessCount = 0;
		} else if ("Searge".equalsIgnoreCase(this.command)) {
			this.chat = new ChatComponentText("#itzlipofutzli");
			this.sucessCount = 1;
		} else {
			MinecraftServer minecraftserver = h();

			if ((minecraftserver != null) && (minecraftserver.M())
					&& (minecraftserver.getEnableCommandBlock())) {
				minecraftserver.getCommandHandler();
				try {
					this.chat = null;

					this.sucessCount = executeCommand(this, this.sender, this.command);
				} catch (Throwable throwable) {
					CrashReport crashreport = CrashReport.a(throwable,
							"Executing command block");
					CrashReportSystemDetails crashreportsystemdetails = crashreport
							.a("Command to be executed");

					crashreportsystemdetails.a("Command", new Callable<Object>() {
						public String a() throws Exception {
							return FixCommandBlockListener.this
									.getCommand();
						}

						public Object call() throws Exception {
							return a();
						}
					});
					crashreportsystemdetails.a("Name", new Callable<Object>() {
						public String a() throws Exception {
							return FixCommandBlockListener.this.getName();
						}

						public Object call() throws Exception {
							return a();
						}
					});
					throw new ReportedException(crashreport);
				}
			} else {
				this.sucessCount = 0;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static int executeCommand(ICommandListener sender,
			CommandSender bSender, String command) {
		SimpleCommandMap commandMap = sender.getWorld().getServer()
				.getCommandMap();
		Joiner joiner = Joiner.on(" ");
		if (command.startsWith("/")) {
			command = command.substring(1);
		}
		String[] args = command.split(" ");
		ArrayList<String[]> commands = new ArrayList<String[]>();

		String cmd = args[0];
		if (cmd.startsWith("minecraft:"))
			cmd = cmd.substring("minecraft:".length());
		if (cmd.startsWith("bukkit:"))
			cmd = cmd.substring("bukkit:".length());

		//Fixes Concerning Plots & Commands
		int whitelistIndex = 0;
		String[] commandWhitelist = {"scoreboard", "testfor", "say", "tell", "tellraw", "achievment", "clear", "effect", "enchant", "execute", "gamemode", "give", "kill", "particle", "playsound", "stopsound", "title", "tp", "xp", "clone", "fill", "setblock", "summon"};
		for(String wElement : commandWhitelist){
			if(cmd.equalsIgnoreCase(wElement)) break;
			whitelistIndex++;
		}
		if(whitelistIndex == commandWhitelist.length) return 0;

		
		Command commandBlockCommand = commandMap.getCommand(args[0]);
		if (sender.getWorld().getServer().getCommandBlockOverride(args[0])) {
			commandBlockCommand = commandMap.getCommand("minecraft:" + args[0]);
		}

		//Execute Commands
		if (commandBlockCommand instanceof VanillaCommandWrapper) {
			command = command.trim();
			if (command.startsWith("/")) {
				command = command.substring(1);
			}
			String[] as = command.split(" ");
			as = VanillaCommandWrapper.dropFirstArgument(as);
			if (!(((VanillaCommandWrapper) commandBlockCommand)
					.testPermission(bSender))) {
				return 0;
			}
			return ((VanillaCommandWrapper) commandBlockCommand)
					.dispatchVanillaCommand(bSender, sender, as);
		}

		if (commandMap.getCommand(args[0]) == null) {
			return 0;
		}

		commands.add(args);

		WorldServer[] prev = MinecraftServer.getServer().worldServer;
		MinecraftServer server = MinecraftServer.getServer();
		server.worldServer = new WorldServer[server.worlds.size()];
		server.worldServer[0] = ((WorldServer) sender.getWorld());
		int bpos = 0;
		for (int pos = 1; pos < server.worldServer.length; ++pos) {
			WorldServer world = (WorldServer) server.worlds.get(bpos++);
			if (server.worldServer[0] == world) {
				--pos;
			} else
				server.worldServer[pos] = world;
		}
		try {
			ArrayList<String[]> newCommands = new ArrayList<String[]>();
			for (int i = 0; i < args.length; ++i)
				if (PlayerSelector.isPattern(args[i])) {
					for (int j = 0; j < commands.size(); ++j) {
						newCommands.addAll(buildCommands(sender,
								(String[]) commands.get(j), i));
					}
					ArrayList<String[]> temp = commands;
					commands = newCommands;
					newCommands = temp;
					newCommands.clear();
				}
		} finally {
			MinecraftServer.getServer().worldServer = prev;
		}

		int completed = 0;

		for (int i = 0; i < commands.size(); ++i) {
			try {
				if (commandMap.dispatch(bSender,
						joiner.join(Arrays.asList((String[]) commands.get(i)))))
					++completed;
			} catch (Throwable exception) {
				if (sender.f() instanceof EntityMinecartCommandBlock) {
					MinecraftServer.getServer().server.getLogger().log(
							Level.WARNING,
							String.format(
									"MinecartCommandBlock at (%d,%d,%d) failed to handle command",
									new Object[] {
											Integer.valueOf(
													sender.getChunkCoordinates()
															.getX()),
											Integer.valueOf(
													sender.getChunkCoordinates()
															.getY()),
											Integer.valueOf(
													sender.getChunkCoordinates()
															.getZ()) }),
							exception);
				} else if (sender instanceof FixCommandBlockListener) {
					FixCommandBlockListener listener = (FixCommandBlockListener) sender;
					MinecraftServer.getServer().server.getLogger().log(
							Level.WARNING,
							String.format(
									"CommandBlock at (%d,%d,%d) failed to handle command",
									new Object[] {
											Integer.valueOf(
													listener.getChunkCoordinates()
															.getX()),
											Integer.valueOf(
													listener.getChunkCoordinates()
															.getY()),
											Integer.valueOf(
													listener.getChunkCoordinates()
															.getZ()) }),
							exception);
				} else {
					MinecraftServer.getServer().server.getLogger().log(
							Level.WARNING,
							String.format(
									"Unknown CommandBlock failed to handle command",
									new Object[0]),
							exception);
				}
			}
		}

		return completed;
	}

	private static ArrayList<String[]> buildCommands(ICommandListener sender,
			String[] args, int pos) {
		ArrayList<String[]> commands = new ArrayList<String[]>();
		List<EntityPlayer> players = PlayerSelector.getPlayers(sender, args[pos],
				EntityPlayer.class);

		if (players != null) {
			for (EntityPlayer player : players) {
				if (player.world != sender.getWorld()) {
					continue;
				}
				String[] command = (String[]) args.clone();
				command[pos] = player.getName();
				commands.add(command);
			}
		}

		return commands;
	}

	public String getName() {
		return this.name;
	}

	public IChatBaseComponent getScoreboardDisplayName() {
		return new ChatComponentText(getName());
	}

	public void setName(String s) {
		this.name = s;
	}

	public void sendMessage(IChatBaseComponent ichatbasecomponent) {
		if ((this.chatOutput) && (getWorld() != null) && (!(getWorld().isClientSide))) {
			this.chat = new ChatComponentText("[" + timeFormat.format(new Date()) + "] ")
					.addSibling(ichatbasecomponent);
			i();
		}
	}

	public boolean getSendCommandFeedback() {
		MinecraftServer minecraftserver = h();

		return ((minecraftserver == null) || (!(minecraftserver.M()))
				|| (minecraftserver.worldServer[0].getGameRules()
						.getBoolean("commandBlockOutput")));
	}

	public void a(
			CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult,
			int i) {
		this.executor.a(h(), this, commandobjectiveexecutor_enumcommandresult, i);
	}

	public abstract void i();

	public void b(IChatBaseComponent ichatbasecomponent) {
		this.chat = ichatbasecomponent;
	}

	public void a(boolean flag) {
		this.chatOutput = flag;
	}

	public boolean n() {
		return this.chatOutput;
	}

	public boolean a(EntityHuman entityhuman) {
		if (!(entityhuman.abilities.canInstantlyBuild)) {
			return false;
		}
		if (entityhuman.getWorld().isClientSide) {
			entityhuman.a(this);
		}

		return true;
	}

	public CommandObjectiveExecutor o() {
		return this.executor;
	}
}