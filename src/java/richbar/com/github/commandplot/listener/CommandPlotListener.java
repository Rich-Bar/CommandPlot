package richbar.com.github.commandplot.listener;

import com.plotsquared.bukkit.events.PlotDeleteEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.plotsquared.bukkit.events.PlayerEnterPlotEvent;
import com.plotsquared.bukkit.events.PlayerLeavePlotEvent;

import richbar.com.github.commandplot.CPlugin;
import richbar.com.github.commandplot.caching.objects.UUIDObject;

public class CommandPlotListener implements Listener{

	private final CPlugin main;

	public CommandPlotListener(CPlugin main){
		this.main = main;
	}


	@EventHandler
	public void onPlotEnter(PlayerEnterPlotEvent event) {
		
	}
	
	@EventHandler
    public void onPlotLeave(PlayerLeavePlotEvent event) {
		main.cbMode.remove(new UUIDObject(event.getPlayer().getUniqueId()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		
	}

	@EventHandler
	public void onPlotDelete(PlotDeleteEvent event){
		/*main.scoreboard.teams.removePlot(event.getPlotId());
		main.scoreboard.objectives.removePlot(event.getPlotId());
		main.scoreboard.scores.removePlot(event.getPlotId());*/
	}
	 
}
