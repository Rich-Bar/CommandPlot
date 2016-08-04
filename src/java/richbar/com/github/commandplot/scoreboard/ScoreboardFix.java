package richbar.com.github.commandplot.scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import richbar.com.github.commandplot.command.BranchingCommand;
import richbar.com.github.commandplot.command.ExecuteSender;
import richbar.com.github.commandplot.scoreboard.objects.ObjectiveObject;
import richbar.com.github.commandplot.scoreboard.visible.VisibleScoreboard;

public class ScoreboardFix extends BranchingCommand{

	private ScoreboardCache cache;

	public ScoreboardFix(FileConfiguration messages, ScoreboardCache cache) {
		super(messages);
		subExecutors.put("players", new ScoreboardPlayers(messages));
		subExecutors.put("objectives", new ScoreboardObjectives(messages));
		subExecutors.put("teams", new ScoreboardTeams(messages));
		this.cache = cache;
	}
	
	private class ScoreboardPlayers extends BranchingCommand{
		public List<String> sub = Arrays.asList(new String[]{"list", "set", "add", "remove", "operation", "reset", "enable", "test", "tag"});
		
		public ScoreboardPlayers(FileConfiguration messages) {
			super(messages);
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			Location loc = getSenderLoc(sender);
			Entity e;
			switch(sub.indexOf(args[0])){
			case 0:
				//TODO: list objectives of certain player (args[1])
				List<String> names = new ArrayList<>();
				for(UUID uuid : cache.scores.getAllPlayers()){
					names.add(Bukkit.getPlayer(uuid).getName());
				}
				sender.sendMessage(messages.getString("players_list") + " " + String.join(", ", names));
				return true;
			case 1:
			case 2:
			case 3:
				e = getEntity(loc.getWorld(),UUID.fromString(args[1]));
				if(e==null) return false;
				Location loce = e.getLocation();
				if(!cache.checker.isSamePlot(loc, loce)) return false;
				if(((CraftEntity)e).getHandle().e(new NBTTagCompound()).toString().matches(args[4])){
					switch(sub.indexOf(args[0])){
					case 1: cache.scores.setScore(((Plot)cache.checker.getPlot(loce)).getId(), e.getUniqueId(), args[2], Integer.parseInt(args[3]));return true;
					case 2: cache.scores.changeScore(((Plot)cache.checker.getPlot(loce)).getId(), e.getUniqueId(), args[2], Integer.parseInt(args[3]));return true;
					case 3: cache.scores.changeScore(((Plot)cache.checker.getPlot(loce)).getId(), e.getUniqueId(), args[2], -1 * Integer.parseInt(args[3]));return true;
					}
				}return false;
			case 4:
				Entity e1 = getEntity(loc.getWorld(),UUID.fromString(args[1]));
				Entity e2 = getEntity(loc.getWorld(),UUID.fromString(args[4]));
				Location loc1 = e1.getLocation();
				Location loc2 = e2.getLocation();
				if(!(cache.checker.isSamePlot(loc, loc1) && cache.checker.isSamePlot(loc, loc2))) return false;
				PlotId pId = ((Plot)cache.checker.getPlot(loc1)).getId();
				List<String> operators = Arrays.asList(new String[]{"+=", "-=", "*=", "/=", "%=", "=", ">", "<"});
				int score1 = cache.scores.getScore(pId, e2.getUniqueId(), args[2]), score2 = cache.scores.getScore(pId, e2.getUniqueId(), args[5]);
				switch(operators.indexOf(args[3])){
				case 0: cache.scores.changeScore(pId, e1.getUniqueId(), args[2], score2);return true;
				case 1: cache.scores.changeScore(pId, e1.getUniqueId(), args[2], -1 * score2);return true;
				case 2: cache.scores.setScore(pId, e1.getUniqueId(), args[2], score1 * score2);return true;
				case 3: cache.scores.setScore(pId, e1.getUniqueId(), args[2], score1 / score2);return true;
				case 4: cache.scores.setScore(pId, e1.getUniqueId(), args[2], score1 % score2);return true;
				case 5: cache.scores.setScore(pId, e1.getUniqueId(), args[2], score2);return true;
				case 6: cache.scores.setScore(pId, e1.getUniqueId(), args[2], score1 > score2? score1 : score2);return true;
				case 7: cache.scores.setScore(pId, e1.getUniqueId(), args[2], score1 < score2? score1 : score2);return true;
				default: return false;
				}
			case 5:
				e = getEntity(loc.getWorld(),UUID.fromString(args[1]));
				if(e==null) return false;
				Location locent = e.getLocation();
				if(!cache.checker.isSamePlot(loc, locent)) return false;
				if(args.length < 3)
					cache.scores.removePlayer(e.getUniqueId());
				else
					cache.scores.removeScore(e.getUniqueId(), ((Plot)cache.checker.getPlot(locent)).getId(), args[2]);
				return true;
			case 6: //TODO: add to trigger list
				return false;
			case 7:
				e = getEntity(loc.getWorld(),UUID.fromString(args[1]));
				if(e==null) return false;
				Location locen = e.getLocation();
				if(!cache.checker.isSamePlot(loc, locen)) return false;
				int min = Integer.parseInt(args[3]), max = Integer.parseInt(args[4]), score = cache.scores.getScore(((Plot)cache.checker.getPlot(locen)).getId(), e.getUniqueId(), args[2]) ;
				return score >= min && score <= max;
			case 8: //TODO: Tag List ( Cache? )
			default:
				sender.sendMessage(messages.getString("subcommands") + " " + String.join(", ", sub));
				return false;
			}
		}
	}
	
	private class ScoreboardObjectives extends BranchingCommand{
		public List<String> sub = Arrays.asList(new String[]{"list", "setdisplay", "remove", "add"});
		
		public ScoreboardObjectives(FileConfiguration messages) {
			super(messages);
		} 
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			Plot plot = ((Plot)cache.checker.getPlot(getSenderLoc(sender)));
			switch(sub.indexOf(args[0])){
			case 0:
				List<String> names = new ArrayList<>();
				for(ObjectiveObject obj : cache.objectives.getAllObjectives(((Plot)cache.checker.getPlot(getSenderLoc(sender))).getId())){
					names.add(obj.name +"("+ obj.displayName +", "+ obj.criteria + ")");
				}
				sender.sendMessage(messages.getString("players_list") + " " + String.join(", ", names));
				return true;
			case 1: 
				for(PlotPlayer p : plot.getPlayersInPlot()){
				Bukkit.getPlayer(p.getUUID()).setScoreboard(new VisibleScoreboard(cache, plot.getId(), null, null, null));
				}
			case 2:
			case 3:
			default:
				sender.sendMessage(messages.getString("subcommands") + " " + String.join(", ", sub));
				return false;
			}
		}
	}

	private class ScoreboardTeams extends BranchingCommand{
		public List<String> sub = Arrays.asList(new String[]{"list", "add", "remove", "empty", "join", "leave", "option"});
		
		public ScoreboardTeams(FileConfiguration messages) {
			super(messages);
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			switch(sub.indexOf(args[0])){
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			default:
				sender.sendMessage(messages.getString("subcommands") + " " + String.join(", ", sub));
				return false;
			}
		}
	}
	
	private Location getSenderLoc(CommandSender sender){
		 Location blockpos = null;
		if(sender instanceof BlockCommandSender) {
	        // Commandblock executed command
			blockpos = ((BlockCommandSender) sender).getBlock().getLocation();
	    }else if(sender instanceof CommandMinecart) {
	        // Minecart Commandlock executed command
	     	blockpos = ((CommandMinecart) sender).getLocation();
	    }else if(sender instanceof ExecuteSender) {
	        // Execute
	        blockpos = ((ExecuteSender) sender).getLocation();
	    }else if(sender instanceof Player) {
	        blockpos = ((Player) sender).getLocation();
	    }
		return blockpos;
	}
	
	public static Entity getEntity(World w, UUID arg){
		for (Entity entity : w.getEntities()) {
            if (entity.getUniqueId().equals(arg))
                return entity;
         }
		return null;
	}
	
}
