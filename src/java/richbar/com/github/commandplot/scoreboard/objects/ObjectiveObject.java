package richbar.com.github.commandplot.scoreboard.objects;

import com.intellectualcrafters.plot.object.PlotId;
import org.bukkit.scoreboard.DisplaySlot;

public class ObjectiveObject{

	public final PlotId id;
	public final String name;
    public final String displayName;
    public final String criteria;
	public DisplaySlot slot = DisplaySlot.SIDEBAR;
	
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
}
