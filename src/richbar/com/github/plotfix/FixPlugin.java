package richbar.com.github.plotfix;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.intellectualcrafters.plot.api.PlotAPI;

public class FixPlugin extends JavaPlugin{
	 public PlotAPI api;

	    @Override
	    public void onEnable() {
	        PluginManager manager = Bukkit.getServer().getPluginManager();
	        final Plugin plotsquared = manager.getPlugin("PlotSquared");

	        if(plotsquared != null && !plotsquared.isEnabled()) {
	            manager.disablePlugin(this);
	            return;
	        }

	        api = new PlotAPI(this);
	    }
	    
	    public boolean isInOwnPlot(Player player){
	    	return api.getPlot(player.getLocation()).isOwner(player.getUniqueId());
	    }

}
