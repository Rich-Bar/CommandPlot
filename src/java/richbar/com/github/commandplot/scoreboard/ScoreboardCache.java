package richbar.com.github.commandplot.scoreboard;

import com.intellectualcrafters.plot.object.PlotId;
import richbar.com.github.commandplot.api.PlotChecker;
import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.caching.ObjectivesCache;
import richbar.com.github.commandplot.scoreboard.caching.ScoreCache;
import richbar.com.github.commandplot.scoreboard.caching.TeamCache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardCache {

	public final TeamCache teams;
	public final ScoreCache scores;
	public final ObjectivesCache objectives;
	public final PlotChecker<?> checker;

    public Map<PlotId, UUID> players = new HashMap<>();
	
	public ScoreboardCache(SQLManager man, PlotChecker<?> checker) {
		teams = new TeamCache(man);
		scores = new ScoreCache(man, this);
		objectives = new ObjectivesCache(man);
		this.checker = checker;
	}

}
