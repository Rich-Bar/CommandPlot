package richbar.com.github.commandplot.scoreboard.objects;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
public class ObjectiveObject extends CacheObject<ObjectiveObject> {

	PlotId id;
	public String name;
	public String displayName;
	public CriteriaObject criteria;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectiveObject fromString(String serialized) {
		// TODO Auto-generated method stub
		return null;
	}

}
