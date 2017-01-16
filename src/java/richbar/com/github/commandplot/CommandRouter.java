package richbar.com.github.commandplot;


import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;
import net.minecraft.server.v1_10_R1.CommandObjectiveExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import richbar.com.github.commandplot.caching.objects.PlotObject;
import richbar.com.github.commandplot.command.ExecuteSender;
import richbar.com.github.commandplot.command.selector.CustomPlayerSelector;
import richbar.com.github.commandplot.util.Util;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CommandRouter extends Command {
	private final Command backend;
	private final CustomPlayerSelector playerSelector;
	private final PSChecker api;
	private final CPlugin main;
	
	public CommandRouter(CPlugin main, Command backend) {
		super(backend.getName(), backend.getDescription(), backend.getUsage(), backend.getAliases());
		this.backend = backend;
		this.main = main;
		playerSelector = new CustomPlayerSelector(main);
		api = new PSChecker();
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(CPlugin.isDebug) System.out.println("Routing Command: " + label + " " + String.join(" ", args) + " - exec by " + sender.getName());
		Location blockpos;
		if (sender instanceof BlockCommandSender) {
			// Commandblock executed command
			blockpos = ((BlockCommandSender) sender).getBlock().getLocation();
		} else if (sender instanceof CommandMinecart) {
			// Minecart Commandlock executed command
			blockpos = ((CommandMinecart) sender).getLocation();
		} else if (sender instanceof ExecuteSender) {
			// Execute
			blockpos = ((ExecuteSender) sender).getLocation();
		} else if (sender instanceof Player) {
			blockpos = ((Player) sender).getLocation();
			if (!api.getPS().getPlotAreaAbs(api.toPSLoc(blockpos)).getPlot(api.toPSLoc(blockpos)).getMembers().contains(((Player) sender).getUniqueId()))
				sender.sendMessage(main.messages.getErrorTypeString("execution-fail-perm").getText());
			return false;
		} else {
			return false;//runCommand(sender, label, args);
		}
		
		
		Plot plot = api.getPlot(blockpos);
		PlotId pId = plot.getId();
		
		if (plot != null && api.getPS().hasPlotArea(blockpos.getWorld().getName())) {
			if (main.getWhitelist().contains(label.toLowerCase()) || main.confWhitelist.keySet().contains(label.toLowerCase())) {
				if (api.canRun(blockpos) || main.activePlots.contains(new PlotObject(pId))) {
					if (main.limiter.check(pId)) {
						if (!main.confBlacklist.contains(label.toLowerCase())) {
							CommandManager commandManager = new CommandManager(main, plot, api);
							
							String argsFlat = String.join(" ", args);
							String targetStrip = argsFlat.substring(argsFlat.indexOf('@'), argsFlat.indexOf(']'));
							List<Entity> children = playerSelector.getEntities(plot, sender, targetStrip);
							if (CPlugin.isDebug) System.out.println("resulted in " + children.size() + " matches");
							boolean res = false;
							int c = 0;
							for (Entity child : children) {
								String newArgs = argsFlat.replace(targetStrip, child.getUniqueId().toString());
								if(newArgs.contains("@")) execute(sender, label, newArgs.split(" "));
								else if (commandManager.execute(sender, label, newArgs.split(" "))) {
									res = true;
									c++;
								}
							}
							Util.getListener(sender).a(CommandObjectiveExecutor.EnumCommandResult.SUCCESS_COUNT, c);
							return res;
						} else sender.sendMessage(main.messages.getErrorTypeString("blacklisted").getText());
					} else sender.sendMessage(main.messages.getErrorTypeString("execution-fail-limit-exceeded").getText());
				} else sender.sendMessage(main.messages.getErrorTypeString("execution-fail-not-active").getText());
			} else sender.sendMessage(main.messages.getErrorTypeString("whitelist-not-contained").getText());
		} else sender.sendMessage(main.messages.getErrorTypeString("not-in-PlotWorld").getText());
		return false;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return backend.tabComplete(sender, alias, args);
	}
	
	public static class PSChecker {
		
		private final PS api = PS.get();
		
		public PS getPS(){
			return api;
		}
		
		public Plot getPlot(Location blockpos) {
			return api.getPlotAreaAbs(toPSLoc(blockpos)).getPlot(toPSLoc(blockpos));
		}
		
		public boolean canRun(Location loc) {
			if(getPlot(loc) == null) return false;
			boolean someOneAlive = false;
			Collection<UUID> someone = getPlot(loc).getMembers();
			someone.addAll(getPlot(loc).getOwners());
			for (UUID idPlayer : someone) {
				PlotPlayer p = PlotPlayer.wrap(idPlayer);
				if (p != null) {
					Location playerLoc = new Location(Bukkit.getWorld(loc.getWorld().getName()), loc.getX(), loc.getY(), loc.getZ());
					if (isSamePlot(loc, playerLoc))
						someOneAlive = true;
				}
			}
			return someOneAlive;
		}
		
		private com.intellectualcrafters.plot.object.Location toPSLoc(Location loc) {
			return new com.intellectualcrafters.plot.object.Location(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		}
		
		public boolean isSamePlot(Location loc1, Location loc2) {
			boolean res;
			Plot p1 = getPlot(loc1), p2 = getPlot(loc2);
			res = p1 != null && p2 != null;
			if (res) res &= p1.getId().toString().equalsIgnoreCase(p2.getId().toString());
			return res;
		}
	}
}
	
