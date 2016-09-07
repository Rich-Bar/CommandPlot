package richbar.com.github.commandplot.scoreboard;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;
import net.minecraft.server.v1_10_R1.MojangsonParseException;
import net.minecraft.server.v1_10_R1.MojangsonParser;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
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
import richbar.com.github.commandplot.command.BranchingCommand;
import richbar.com.github.commandplot.command.ExecuteSender;
import richbar.com.github.commandplot.scoreboard.objects.ObjectiveObject;
import richbar.com.github.commandplot.scoreboard.objects.TeamObject;
import richbar.com.github.commandplot.scoreboard.visible.VisibleScoreboard;
import richbar.com.github.commandplot.util.TeamColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ScoreboardFix extends BranchingCommand{

	private ScoreboardCache cache;

	public ScoreboardFix(FileConfiguration messages, ScoreboardCache cache) {
		super(messages, "scoreboard");
		subExecutors.put("players", new ScoreboardPlayers(messages));
		subExecutors.put("objectives", new ScoreboardObjectives(messages));
		subExecutors.put("teams", new ScoreboardTeams(messages));
		this.cache = cache;
	}
	
	private class ScoreboardPlayers extends BranchingCommand{
		public List<String> sub = Arrays.asList("list", "set", "add", "remove", "operation", "reset", "enable", "test", "tag");
		
		public ScoreboardPlayers(FileConfiguration messages) {
			super(messages, "scoreboard");
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			Location loc = getSenderLoc(sender);
			Location entityLoc;
			Entity e;

			if(sender instanceof Player) {
				if (!(((Plot)cache.checker.getPlot(loc)).getMembers().contains(((Player) sender).getUniqueId())))
					return false;
			}
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
					e = getEntity(loc.getWorld(), UUID.fromString(args[1]));
					if(e==null) return false;
					entityLoc = e.getLocation();

					if(args.length < 4 || !cache.checker.isSamePlot(loc, entityLoc)) return false;
					if(args.length == 5 && !checkNBT(e, args[4])) return false;

					cache.scores.setScore(((Plot)cache.checker.getPlot(loc)).getId(), e.getUniqueId(), args[2], Integer.parseInt(args[3]));
					return true;
				case 2:
					e = getEntity(loc.getWorld(), UUID.fromString(args[1]));
					if(e==null) return false;
					entityLoc = e.getLocation();

					if(args.length < 4 || !cache.checker.isSamePlot(loc, entityLoc)) return false;
					if(args.length == 5 && !checkNBT(e, args[4])) return false;

					cache.scores.changeScore(((Plot)cache.checker.getPlot(loc)).getId(), e.getUniqueId(), args[2], Integer.parseInt(args[3]));
					return true;
				case 3:
					e = getEntity(loc.getWorld(),UUID.fromString(args[1]));
					if(e==null) return false;
					entityLoc = e.getLocation();

					if(!cache.checker.isSamePlot(loc, entityLoc)) return false;
					if(((CraftEntity)e).getHandle().e(new NBTTagCompound()).toString().matches(args[4])){
						switch(sub.indexOf(args[0])){
						case 1: cache.scores.setScore(((Plot)cache.checker.getPlot(entityLoc)).getId(), e.getUniqueId(), args[2], Integer.parseInt(args[3]));return true;
						case 2: cache.scores.changeScore(((Plot)cache.checker.getPlot(entityLoc)).getId(), e.getUniqueId(), args[2], Integer.parseInt(args[3]));return true;
						case 3: cache.scores.changeScore(((Plot)cache.checker.getPlot(entityLoc)).getId(), e.getUniqueId(), args[2], -1 * Integer.parseInt(args[3]));return true;
						}
					}return false;
				case 4:
					Entity e1 = getEntity(loc.getWorld(),UUID.fromString(args[1]));
					Entity e2 = getEntity(loc.getWorld(),UUID.fromString(args[4]));
					Location loc1 = e1.getLocation();
					Location loc2 = e2.getLocation();

					if(!(cache.checker.isSamePlot(loc, loc1) && cache.checker.isSamePlot(loc, loc2))) return false;
					PlotId pId = ((Plot)cache.checker.getPlot(loc1)).getId();
					List<String> operators = Arrays.asList("+=", "-=", "*=", "/=", "%=", "=", ">", "<");
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
					entityLoc = e.getLocation();
					if(!cache.checker.isSamePlot(loc, entityLoc)) return false;

					if(args.length < 3) cache.scores.removePlayer(e.getUniqueId());
					else cache.scores.removeScore(e.getUniqueId(), ((Plot)cache.checker.getPlot(entityLoc)).getId(), args[2]);
					return true;
				case 6:
					//TODO: add to trigger list
					return false;
				case 7:
					e = getEntity(loc.getWorld(),UUID.fromString(args[1]));
					if(e==null) return false;
					entityLoc = e.getLocation();
					if(!cache.checker.isSamePlot(loc, entityLoc)) return false;

					int min = Integer.parseInt(args[3]), max = Integer.parseInt(args[4]), score = cache.scores.getScore(((Plot)cache.checker.getPlot(entityLoc)).getId(), e.getUniqueId(), args[2]) ;
					return score >= min && score <= max;
				case 8:
					//TODO: Tag List ( Cache? )
				default:
					sender.sendMessage(messages.getString("subcommands") + " " + String.join(", ", sub));
					return false;
			}
		}
	}
	
	private class ScoreboardObjectives extends BranchingCommand{
		List<String> sub = Arrays.asList("list", "setdisplay", "remove", "add");
		
		ScoreboardObjectives(FileConfiguration messages) {
			super(messages, "scoreboard");
		} 
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			Plot plot = ((Plot)cache.checker.getPlot(getSenderLoc(sender)));

			if(sender instanceof Player) {
				if (!(plot.getMembers().contains(((Player) sender).getUniqueId())))
					return false;
			}
			switch(sub.indexOf(args[0])){
				case 0:
					//TODO: SHOW OBJECTIVE INFO
					List<String> names = new ArrayList<>();
					for(ObjectiveObject obj : cache.objectives.getAllObjectives(plot.getId())){
						names.add(obj.name +"("+ obj.displayName +", "+ obj.criteria + ")");
					}
					sender.sendMessage(messages.getString("players_list") + " " + String.join(", ", names));
					return true;
				case 1:
					//TODO: VISIBLE SCOREBOARD
					switch(args[1].lastIndexOf(".") == 12?args[1].substring(0, 12).toLowerCase() : args[1].toLowerCase()){
					case "sidebar.team":
					case "sidebar":
					case "list":
					case "belowname":
					}
					for(PlotPlayer p : plot.getPlayersInPlot()){
					Bukkit.getPlayer(p.getUUID()).setScoreboard(new VisibleScoreboard(cache, plot.getId(), null, null, null));
					}
					return true;
				case 2:
					if(args.length < 2) return false;
					cache.objectives.removeObjective(plot.getId(), args[1]);
					return true;
				case 3:
					if(args.length < 3) return false;
					cache.objectives.addObjective(plot.getId(), new ObjectiveObject(plot.getId(), args[1], args.length <= 3? args[1] : args[3] , args[2]));
					return true;
				default:
					sender.sendMessage(messages.getString("subcommands") + " " + String.join(", ", sub));
					return false;
			}
		}
	}

	private class ScoreboardTeams extends BranchingCommand{
		public List<String> sub = Arrays.asList("list", "add", "remove", "empty", "join", "leave", "option");
		
		public ScoreboardTeams(FileConfiguration messages) {
			super(messages, "scoreboard");
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			Plot plot = ((Plot)cache.checker.getPlot(getSenderLoc(sender)));
			Location loc = getSenderLoc(sender);
			TeamObject team;
			Location entityLoc;
			Entity e;

			if(sender instanceof Player) {
				if (!(plot.getMembers().contains(((Player) sender).getUniqueId())))
					return false;
			}
			switch(sub.indexOf(args[0])){
				case 0:
					//TODO: LIST ATTRIBUTES OF TEAM
					int i = 0;
					List<String> names = new ArrayList<>();
					for(TeamObject obj : cache.teams.getAllTeams(plot.getId())){
						names.add(i + ": " + obj.name +"("+ obj.displayName +")");
					}
					sender.sendMessage(messages.getString("teams_list") + " " + String.join(", ", names));
					return true;
				case 1:
					if(args.length < 3) return false;
					cache.teams.addTeam(plot.getId(), new TeamObject(plot.getId(), args[1], args.length == 2? args[1] : args[2], TeamColor.NONE, 0));
					return true;
				case 2:
					if(args.length < 2) return false;
					cache.teams.removeTeam(plot.getId(), args[1]);
					return true;
				case 3:
					 team = cache.teams.getTeam(plot.getId(), args[1]);
					if(team == null){
						sender.sendMessage(messages.getString("no-team-found"));
						return false;
					}
					for(UUID uuid : team.members){
						cache.scores.setScore(plot.getId(), uuid, "team." + plot.getId(), -1);
					}
					team.members = new ArrayList<>();
					return true;
				case 4:
					team = cache.teams.getTeam(plot.getId(), args[1]);
					if(sender instanceof Player) {
						if (args.length == 2) {
							cache.scores.setScore(plot.getId(), ((Player) sender).getUniqueId(), "team." + plot.getId(), Integer.parseInt(args[1]));
							team.members.add(((Player) sender).getUniqueId());
						} else if (args.length == 3) {
							cache.scores.setScore(plot.getId(), UUID.fromString(args[2]), "team." + plot.getId(), Integer.parseInt(args[1]));
							team.members.add(UUID.fromString(args[2]));
						}else return false;
					}else if (args.length == 3) {
						cache.scores.setScore(plot.getId(), UUID.fromString(args[2]), "team." + plot.getId(), Integer.parseInt(args[1]));
						team.members.add(UUID.fromString(args[2]));
					}else return false;
					return true;
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


	private boolean checkNBT(Entity e, String arg){
		try {
			NBTTagCompound compound = MojangsonParser.parse(arg);
			NBTTagCompound content = ((CraftEntity)e).getHandle().e(new NBTTagCompound());
			return checkNBT(content, compound);
		}catch(MojangsonParseException exc){
			return false;
		}
	}

	private boolean checkNBT(NBTTagCompound content, NBTTagCompound compound){
		boolean res = true;
		for(String name : compound.c()){
			if(content.hasKey(name))
				if(content.get(name) instanceof  NBTTagCompound)
					res = res && checkNBT((NBTTagCompound)content.get(name), (NBTTagCompound)compound.get(name));
				else
					res = res && content.get(name).equals(compound.get(name));
			else return false;
		}
		return res;
	}
}
