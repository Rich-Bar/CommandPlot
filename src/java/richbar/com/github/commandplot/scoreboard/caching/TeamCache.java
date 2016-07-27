package richbar.com.github.commandplot.scoreboard.caching;

import richbar.com.github.commandplot.caching.CacheObject;
import richbar.com.github.commandplot.caching.sql.SQLCache;
import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.objects.PlotBoardObject;
import richbar.com.github.commandplot.scoreboard.objects.TeamObject;

public class TeamCache{

	public TeamCache(SQLManager man, TeamWrapper wrap) {
		
	}
	
	public boolean addObject(CacheObject<PlotBoardObject> elem) {
		return super.addObject(elem);
	}
	
	public boolean remove(CacheObject<PlotBoardObject> elem) {
		return super.remove(elem);
	}
	
	public boolean contains(CacheObject<PlotBoardObject> elem) {
		return super.contains(elem);
	}
	
	public void loadFromBackend() {
		super.loadFromBackend();
	}
}
