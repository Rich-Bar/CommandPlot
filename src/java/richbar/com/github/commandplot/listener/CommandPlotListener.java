package richbar.com.github.commandplot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.plotsquared.bukkit.events.PlayerEnterPlotEvent;
import com.plotsquared.bukkit.events.PlayerLeavePlotEvent;

public class CommandPlotListener implements Listener{
	
	@EventHandler
	public void onPlotEnter(PlayerEnterPlotEvent event) {
		
	}
	
	@EventHandler
    public void onPlotLeave(PlayerLeavePlotEvent event) {
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		
	}
	 
}
