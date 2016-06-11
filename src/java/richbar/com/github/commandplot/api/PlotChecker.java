package richbar.com.github.commandplot.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class PlotChecker<A> {
	
	A api;
	
	public PlotChecker(A api) {
		this.api = api;
	}
	
	public abstract boolean isInPlot(Player p);
	
	public abstract boolean canRun(Location loc);
	
	public abstract boolean isSamePlot(Location loc1, Location loc2);
	
	public abstract boolean isPlotWorld(World world);
	
	public abstract A getAPI();
	
	public abstract Object getPlot(Location loc);
}
