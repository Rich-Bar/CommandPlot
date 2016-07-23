package richbar.com.github.commandplot.scoreboard;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import richbar.com.github.commandplot.command.BranchingCommand;

public class ScoreboardFix extends BranchingCommand{

	public ScoreboardFix(FileConfiguration messages) {
		super(messages);
		subExecutors.put("players", new ScoreboardPlayers(messages));
		subExecutors.put("objectives", new ScoreboardObjectives(messages));
		subExecutors.put("teams", new ScoreboardTeams(messages));
	}
	
	private class ScoreboardPlayers extends BranchingCommand{
		public List<String> sub = Arrays.asList(new String[]{"list", "set", "add", "remove", "reset", "enable", "test", "tag"});
		
		public ScoreboardPlayers(FileConfiguration messages) {
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
			case 7:
			}
			return true;
		}
	}
	
	private class ScoreboardObjectives extends BranchingCommand{
		public List<String> sub = Arrays.asList(new String[]{"list", "setdisplay", "remove", "add"});
		
		public ScoreboardObjectives(FileConfiguration messages) {
			super(messages);
		} 
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			switch(sub.indexOf(args[0])){
			case 0:
			case 1:
			case 2:
			case 3:
			}
			return true;
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
			}
			return true;
		}
	}
	

}
