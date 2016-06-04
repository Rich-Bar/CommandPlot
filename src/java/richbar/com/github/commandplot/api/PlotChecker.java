package richbar.com.github.commandplot.api;

import org.bukkit.Location;
import org.bukkit.World;

public abstract class PlotChecker<A> {
	
	public PlotChecker(A api) {}
	
	public abstract boolean canRun(Location loc);
	
	public abstract boolean isSamePlot(Location loc1, Location loc2);
	
	public abstract boolean isPlotWorld(World world);
	
	public abstract A getAPI();
	
}
