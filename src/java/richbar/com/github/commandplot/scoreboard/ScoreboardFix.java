package richbar.com.github.commandplot.scoreboard;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;

import net.minecraft.server.v1_10_R1.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.command.BranchingCommand;
import richbar.com.github.commandplot.scoreboard.objects.ObjectiveObject;
import richbar.com.github.commandplot.scoreboard.objects.TeamObject;
import richbar.com.github.commandplot.scoreboard.visible.VisibleScoreboard;
import richbar.com.github.commandplot.util.TeamColor;
import richbar.com.github.commandplot.util.Util;

import java.util.*;

public class ScoreboardFix extends BranchingCommand{

	private final ScoreboardCache cache;

	public ScoreboardFix(FileConfiguration messages, ScoreboardCache cache) {
		super(messages, "scoreboard");
		subExecutors.put("players", new ScoreboardPlayers(messages));
		subExecutors.put("objectives", new ScoreboardObjectives(messages));
		subExecutors.put("teams", new ScoreboardTeams(messages));
		this.cache = cache;
	}
	
	private class ScoreboardPlayers extends BranchingCommand{
		final List<String> sub = Arrays.asList("list", "set", "add", "remove", "operation", "reset", "enable", "test", "tag");
		
		ScoreboardPlayers(FileConfiguration messages) {
			super(messages, "scoreboard");
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			Location loc = Util.getSenderLoc(sender);
			Location entityLoc;
			Entity e;

			if(sender instanceof Player) {
				if (!(((Plot)cache.checker.getPlot(loc)).getMembers().contains(((Player) sender).getUniqueId())))
					return false;
			}
			switch(sub.indexOf(args[0])){
				case 0:
					//TEST: list objectives of certain player (args[1])
					if(args.length > 1){
						Player p = Util.getPlayer(args[1]);
						if(p == null) return false;
						Map<String, Integer> allScores = cache.scores.getAllScores(p.getUniqueId());
						List<String> plainScores = new ArrayList<>();
						for(Map.Entry score : allScores.entrySet()){
							plainScores.add(score.getKey() + " = " + score.getValue());
						}
						sender.sendMessage(messages.getString("player_scores") + " " + String.join(", ", plainScores));
						return true;
					}else{
						List<String> names = new ArrayList<>();
						for (UUID uuid : cache.scores.getAllPlayers()) {
							names.add(Bukkit.getPlayer(uuid).getName());
						}
						sender.sendMessage(messages.getString("players_list") + " " + String.join(", ", names));
						return true;
					}
				case 1:
					e = Util.getEntity(loc.getWorld(), UUID.fromString(args[1]));
					if(e==null) e = Util.getPlayer(args[1]);
					if(e==null) return false;
					entityLoc = e.getLocation();

					if(args.length < 4 || !cache.checker.isSamePlot(loc, entityLoc)) return false;
					if(args.length == 5 && !Util.checkNBT(e, args[4])) return false;

					cache.scores.setScore(((Plot)cache.checker.getPlot(loc)).getId(), e.getUniqueId(), args[2], Integer.parseInt(args[3]));
					return true;
				case 2:
					e = Util.getEntity(loc.getWorld(), UUID.fromString(args[1]));
					if(e==null) e = Util.getPlayer(args[1]);
					if(e==null) return false;
					entityLoc = e.getLocation();

					if(args.length < 4 || !cache.checker.isSamePlot(loc, entityLoc)) return false;
					if(args.length == 5 && !Util.checkNBT(e, args[4])) return false;

					cache.scores.changeScore(((Plot)cache.checker.getPlot(loc)).getId(), e.getUniqueId(), args[2], Integer.parseInt(args[3]));
					return true;
				case 3:
					e = Util.getEntity(loc.getWorld(),UUID.fromString(args[1]));
					if(e==null) e = Util.getPlayer(args[1]);
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
					Entity e1 = Util.getEntity(loc.getWorld(),UUID.fromString(args[1]));
					Entity e2 = Util.getEntity(loc.getWorld(),UUID.fromString(args[4]));
					if(e1==null) e1 = Util.getPlayer(args[1]);
					if(e1==null) return false;
					if(e2==null) e2 = Util.getPlayer(args[4]);
					if(e2==null) return false;
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
					e = Util.getEntity(loc.getWorld(),UUID.fromString(args[1]));
					if(e==null) e = Util.getPlayer(args[1]);
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
					e = Util.getEntity(loc.getWorld(),UUID.fromString(args[1]));
					if(e==null) e = Util.getPlayer(args[1]);
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
		final List<String> sub = Arrays.asList("list", "setdisplay", "remove", "add");
		
		ScoreboardObjectives(FileConfiguration messages) {
			super(messages, "scoreboard");
		} 
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			Plot plot = ((Plot)cache.checker.getPlot(Util.getSenderLoc(sender)));

			if(sender instanceof Player) {
				if (!(plot.getMembers().contains(((Player) sender).getUniqueId())))
					return false;
			}
			switch(sub.indexOf(args[0])){
				case 0:
					//TEST: SHOW OBJECTIVE INFO
					if(args.length > 1){
						ObjectiveObject obj = cache.objectives.getObjectiveByName(plot.getId(), args[1]);
						if(obj == null) return false;
						String objInfo = obj.id + ": " + obj.name + "[" + obj.displayName + "] - " + obj.criteria;
						sender.sendMessage(messages.getString("objective_info") + " " + objInfo);
						return true;
					}else {
						List<String> names = new ArrayList<>();
						for (ObjectiveObject obj : cache.objectives.getAllObjectives(plot.getId())) {
							names.add(obj.name + "(" + obj.displayName + ", " + obj.criteria + ")");
						}
						sender.sendMessage(messages.getString("players_list") + " " + String.join(", ", names));
						return true;
					}
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
		final List<String> sub = Arrays.asList("list", "add", "remove", "empty", "join", "leave", "option");
		
		ScoreboardTeams(FileConfiguration messages) {
			super(messages, "scoreboard");
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			Plot plot = ((Plot)cache.checker.getPlot(Util.getSenderLoc(sender)));
			TeamObject team;

			if(sender instanceof Player) {
				if (!(plot.getMembers().contains(((Player) sender).getUniqueId())))
					return false;
			}
			switch(sub.indexOf(args[0])){
				case 0:
					//TEST: LIST ATTRIBUTES OF TEAM
					if(args.length > 1){
						team = cache.teams.getTeam(plot.getId(), args[1]);
						if(team == null) return false;
						String teamTitle = plot.getId() + ": " + team.name + " / " + ChatColor.translateAlternateColorCodes('&', "&" + team.color.getIndex() + team.displayName) + ":";
						String teamData = "";
						teamData += team.nameTagsOwnTeam && team.nameTagsOtherTeam? "Nametags: both, " :
									team.nameTagsOwnTeam? "Nametags: own Team, " :
									team.nameTagsOtherTeam? "Nametags: other Team, " : "Nametags: none, ";
						teamData += team.collisionOwnTeam && team.collisionOtherTeams ? "Collision: both, " :
									team.collisionOwnTeam? "Collision: own Team, " :
									team.collisionOtherTeams ? "Collision: other Team, " : "Collision: none, ";
						teamData += team.deathMessageOwnTeam && team.deathMessageOtherTeams? "Deathmessage: both, " :
									team.deathMessageOwnTeam? "Deathmessage: own Team, " :
									team.deathMessageOtherTeams? "Deathmessage: other Team, " : "Deathmessage: none, ";
						teamData += team.allowFriendlyFire? "Friendly Fire: enabled, " : "Friendly Fire: disabled, ";
						teamData += team.SeeFriendlyInvisibles? "See Friendly Invisibles: enabled" : "See Friendly Invisibles: disabled";
						sender.sendMessage(teamTitle);
						sender.sendMessage(teamData);
						return true;
					}else {
						int i = 0;
						List<String> names = new ArrayList<>();
						for (TeamObject obj : cache.teams.getAllTeams(plot.getId())) {
							names.add(i + ": " + obj.name + "(" + obj.displayName + ")");
						}
						sender.sendMessage(messages.getString("teams_list") + " " + String.join(", ", names));
						return true;
					}
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
							if(args[2].equals("*")) sender.sendMessage("Please Use @a instead of '*'");
							cache.scores.setScore(plot.getId(), Util.getPlayer(args[2]).getUniqueId(), "team." + plot.getId(), Integer.parseInt(args[1]));
							team.members.add(Util.getPlayer(args[2]).getUniqueId());
						}else return false;
					}else if (args.length == 3) {
						cache.scores.setScore(plot.getId(), Util.getPlayer(args[2]).getUniqueId(), "team." + plot.getId(), Integer.parseInt(args[1]));
						team.members.add(Util.getPlayer(args[2]).getUniqueId());
					}else return false;
					return true;
				case 5:
					if(args.length == 2) {
						if (args[1].equals("*")) sender.sendMessage("Please Use @a instead of '*'");
						Player p = Util.getPlayer(args[1]);
						cache.scores.removeScore(p.getUniqueId(), plot.getId(), "team." + plot.getId());
						for(TeamObject obj : cache.teams.getAllTeams(plot.getId())){
							if(obj.members.contains(p.getUniqueId())) {
								obj.members.remove(p.getUniqueId());
								break;
							}
						}
					}else if(sender instanceof Player){
						Player p = (Player) sender;
						cache.scores.removeScore(p.getUniqueId(), plot.getId(), "team." + plot.getId());
						for(TeamObject obj : cache.teams.getAllTeams(plot.getId())){
							if(obj.members.contains(p.getUniqueId())) {
								obj.members.remove(p.getUniqueId());
								break;
							}
						}
					}else return false;
					return true;
				case 6:
					//TODO: Apply Settings in game
					team = cache.teams.getTeam(plot.getId(), args[1]);
					switch(args[2]){
						case "collisionRule":
							if(args[3].toLowerCase().contains("never")) team.collisionOwnTeam = team.collisionOtherTeams = false;
							if(args[3].toLowerCase().contains("otherteam")){ team.collisionOwnTeam = false; team.collisionOtherTeams = true;}
							if(args[3].toLowerCase().contains("ownteam")){ team.collisionOwnTeam = true; team.collisionOtherTeams = false;}
							if(args[3].toLowerCase().contains("always")) team.collisionOwnTeam = team.collisionOtherTeams = true;
							return true;
						case "color":
						case "deathMessageVisibility":
							if(args[3].toLowerCase().contains("never")) team.deathMessageOwnTeam = team.deathMessageOtherTeams = false;
							if(args[3].toLowerCase().contains("otherteam")){ team.deathMessageOwnTeam = false; team.deathMessageOtherTeams = true;}
							if(args[3].toLowerCase().contains("ownteam")){ team.deathMessageOwnTeam = true; team.deathMessageOtherTeams = false;}
							if(args[3].toLowerCase().contains("always")) team.deathMessageOwnTeam = team.deathMessageOtherTeams = true;
							return true;
						case "friendlyfire":
						case "nametagVisibility":
							if(args[3].toLowerCase().contains("never")) team.nameTagsOwnTeam = team.nameTagsOtherTeam = false;
							if(args[3].toLowerCase().contains("otherteam")){ team.nameTagsOwnTeam = false; team.nameTagsOtherTeam = true;}
							if(args[3].toLowerCase().contains("ownteam")){ team.nameTagsOwnTeam = true; team.nameTagsOtherTeam = false;}
							if(args[3].toLowerCase().contains("always")) team.nameTagsOwnTeam = team.nameTagsOtherTeam = true;
							return true;
						case "seeFriendlyInvisibles":
					}
				default:
					sender.sendMessage(messages.getString("subcommands") + " " + String.join(", ", sub));
					return false;
			}
		}
	}

}
