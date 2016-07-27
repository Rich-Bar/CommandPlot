package richbar.com.github.commandplot.scoreboard;

import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.caching.ObjectivesCache;
import richbar.com.github.commandplot.scoreboard.caching.ObjectivesWrapper;
import richbar.com.github.commandplot.scoreboard.caching.TeamCache;
import richbar.com.github.commandplot.scoreboard.caching.TeamWrapper;

public class ScoreboardCache {

	public TeamCache plots;
	public ObjectivesCache objectives;
	
	public ScoreboardCache(SQLManager man) {
		plots = new TeamCache(man, new TeamWrapper());
		objectives = new ObjectivesCache(man, new ObjectivesWrapper());
	}
	
}
