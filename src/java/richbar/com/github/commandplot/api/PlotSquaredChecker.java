package richbar.com.github.commandplot.api;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;

public class PlotSquaredChecker extends PlotChecker<PS>{
	
	public PlotSquaredChecker(PS ps) {
		super(ps);
	}
	
	public boolean canRun(Location loc){
		boolean someOneAlive = false;
		Collection<UUID> someone = getPlot(loc).getMembers();
		someone.addAll(getPlot(loc).getOwners());
		for(UUID idPlayer : someone){
			PlotPlayer p = PlotPlayer.wrap(idPlayer);
			Location playerLoc = toBukkitLoc(p.getLocation());
			if(isSamePlot(loc, playerLoc)) 
				someOneAlive = true;
		}
		return someOneAlive;
    }
    	
	private Location toBukkitLoc(com.intellectualcrafters.plot.object.Location loc){
		return new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
	}
	
	private com.intellectualcrafters.plot.object.Location toPSLoc(Location loc){
		return new com.intellectualcrafters.plot.object.Location(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	public Plot getPlot(Location loc){
		return api.getPlotAreaAbs(toPSLoc(loc)).getPlot(toPSLoc(loc));
	}
	
	public boolean isSamePlot(Location loc1, Location loc2){
		boolean res;
		Plot p1 = getPlot(loc1), p2 = getPlot(loc2);
		res = p1 != null && p2 !=null;
		res = res && p1.getId().toString().equalsIgnoreCase(p2.getId().toString());
		return res;
	}
	
	public PS getAPI(){
		return api;
	}
	
	@Override
	public boolean isPlotWorld(World world) {
		return api.hasPlotArea(world.getName());
	}

	@Override
	public boolean isInPlot(Player p) {
		return api.getPlots(p.getWorld().getName(), PlotPlayer.wrap(p.getUniqueId())).contains(getPlot(p.getLocation()));
	}
	
}
