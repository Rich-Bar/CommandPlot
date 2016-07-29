package richbar.com.github.commandplot.scoreboard;

import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.caching.ObjectivesCache;
import richbar.com.github.commandplot.scoreboard.caching.ScoreCache;
import richbar.com.github.commandplot.scoreboard.caching.TeamCache;

public class ScoreboardCache {

	public TeamCache teams;
	public ScoreCache scores;
	public ObjectivesCache objectives;
	
	public ScoreboardCache(SQLManager man) {
		teams = new TeamCache(man);
		scores = new ScoreCache(man);
		objectives = new ObjectivesCache(man);
	}
	
}
