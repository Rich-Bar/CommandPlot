package richbar.com.github.commandplot.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.intellectualcrafters.plot.api.PlotAPI;

public class PlotAPIChecker {

	private PlotAPI api;
	
	public PlotAPIChecker(PlotAPI api) {
		this.api = api;
	}
	
	public boolean canRun(Location loc){
		boolean someOneAlive = false;
		for(UUID idPlayer : api.getPlot(loc).getMembers()){
			Location playerLoc = toBukkitLoc(api.wrapPlayer(idPlayer).getLocation());
			System.out.println(playerLoc);
			if(isSamePlot(loc, playerLoc)) 
				someOneAlive = true;
		}
		return someOneAlive;
    }
    	
	public Location toBukkitLoc(com.intellectualcrafters.plot.object.Location loc){
		System.out.println(Bukkit.getWorld(loc.getWorld()));
		return new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
	}
	
	public boolean isSamePlot(Location loc1, Location loc2){
		return api.getPlot(loc1).getId() == api.getPlot(loc2).getId();
	}
	
	public PlotAPI getAPI(){
		return api;
	}
	
}
