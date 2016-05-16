package richbar.com.github.commandplot;

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
        
        for(Commands command : Commands.values()){
	        String cmd = command.getInst().getCommand().toLowerCase();
	        System.out.println("Registering Command " + cmd);
	        this.getCommand(cmd).setExecutor(cmdMan);
	        System.out.println("- Done!");
	    }
    }

	public boolean canRun(Location loc){
    	return api.getPlot(loc).isOnline();
    }
    
	public boolean isSamePlot(Location loc1, Location loc2){
		return api.getPlot(loc1).getId() == api.getPlot(loc2).getId();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
