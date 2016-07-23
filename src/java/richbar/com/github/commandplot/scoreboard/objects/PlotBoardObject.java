package richbar.com.github.commandplot.scoreboard.objects;


import java.util.ArrayList;
import java.util.List;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
public class PlotBoardObject extends CacheObject<PlotBoardObject>{
	
	PlotId plotId;
	
	public List<ObjectiveObject> plotObjectives = new ArrayList<>();
	public List<TeamObject> plotTeams = new ArrayList<>();
	
	public String[] displayslots = new String[19];

	@Override
	public String toString() {
		return null;
	}

	@Override
	public PlotBoardObject fromString(String serialized) {
		return null;
	}
}
