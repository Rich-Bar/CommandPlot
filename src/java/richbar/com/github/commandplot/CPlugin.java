package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.intellectualcrafters.plot.api.PlotAPI;

import richbar.com.github.commandplot.CommandManager.Commands;

public class CPlugin extends JavaPlugin{

	public PlotAPI api;
    private CommandManager cmdMan;
    private List<String> whitelist = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@Override
    public void onEnable() {
		cmdMan = new CommandManager(this);
		
        PluginManager manager = Bukkit.getServer().getPluginManager();
        
        final Plugin plotsquared = manager.getPlugin("PlotSquared");
        if(plotsquared != null && !plotsquared.isEnabled()) {
            manager.disablePlugin(this);
            return;
        }
        api = new PlotAPI(this);
        
        manager.registerEvents(new CommandAccessor(api), this);
        
        for(Commands command : Commands.values()){
	        String cmd = command.getInst().getCommand().toLowerCase();
	        System.out.println("Registering Command " + cmd);
	        whitelist.add(cmd);
	        this.getCommand(cmd).setExecutor(cmdMan);
	    }
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
    
	public boolean isSamePlot(Location loc1, Location loc2){
		return api.getPlot(loc1).getId() == api.getPlot(loc2).getId();
	}
	public List<String> getWhitelist(){
		return whitelist;
	}
	
	public Location toBukkitLoc(com.intellectualcrafters.plot.object.Location loc){
		System.out.println(Bukkit.getWorld(loc.getWorld()));
		return new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
