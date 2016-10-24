package richbar.com.github.commandplot.scoreboard;

import richbar.com.github.commandplot.api.PlotChecker;
import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.caching.ObjectivesCache;
import richbar.com.github.commandplot.scoreboard.caching.ScoreCache;
import richbar.com.github.commandplot.scoreboard.caching.TeamCache;

public class ScoreboardCache {

	public final TeamCache teams;
	public final ScoreCache scores;
	public final ObjectivesCache objectives;
	public final PlotChecker<?> checker;
	
	public ScoreboardCache(SQLManager man, PlotChecker<?> checker) {
		teams = new TeamCache(man);
		scores = new ScoreCache(man, this);
		objectives = new ObjectivesCache(man);
		this.checker = checker;
	}

}
