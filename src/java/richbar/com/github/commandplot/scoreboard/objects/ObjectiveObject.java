package richbar.com.github.commandplot.scoreboard.objects;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
public class ObjectiveObject extends CacheObject<ObjectiveObject> {

	public PlotId id;
	public String name, displayName, criteria;
	
	public ObjectiveObject() {
		super();
	}
	
	public ObjectiveObject(String parse) {
		super(parse);
	}
	
	public ObjectiveObject(PlotId pId, String name, String displayName, String criteria) {
		this.id = pId;
		this.name = name;
		this.displayName = displayName;
		this.criteria = criteria;
	}
	
	@Override
	public String toString() {
		return id.toString() + ";" + name + ";" + displayName + ";" + criteria;
	}

	@Override
	public ObjectiveObject fromString(String serialized) {
		String[] args = serialized.split(";", 5);
		id = new PlotId(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		name = args[2];
		displayName = args[3];
		criteria = args[4];
		return this;
	}
	
	@Override
	public ObjectiveObject getObject() {
		return this;
	}

}
