package richbar.com.github.commandplot.listener;

import com.plotsquared.bukkit.events.PlotDeleteEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.plotsquared.bukkit.events.PlayerLeavePlotEvent;

import richbar.com.github.commandplot.CPlugin;
import richbar.com.github.commandplot.backends.CommandBlockMode;
import richbar.com.github.commandplot.caching.objects.UUIDObject;

public class CommandPlotListener implements Listener{

	private final CPlugin main;
    private final CommandBlockMode cbMode;

    public CommandPlotListener(CPlugin main, CommandBlockMode cbMode){
		this.main = main;
        this.cbMode = cbMode;
	}
	
	@EventHandler
    public void onPlotLeave(PlayerLeavePlotEvent event) {
        Player p = event.getPlayer();
        if(cbMode.contains(new UUIDObject(p.getUniqueId()))){
            if(!p.hasPermission("plots.commandplot.admin"))p.setOp(false);
            p.sendMessage(main.messages.getColoredString("cbm-leave"));
            cbMode.remove(new UUIDObject(p.getUniqueId()));
        }
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if(cbMode.contains(new UUIDObject(p.getUniqueId()))){
            if(!p.hasPermission("plots.commandplot.admin"))p.setOp(false);
            p.sendMessage(main.messages.getColoredString("cbm-leave"));
            cbMode.remove(new UUIDObject(p.getUniqueId()));
        }
	}


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        if(cbMode.contains(new UUIDObject(p.getUniqueId()))){
            if(!p.hasPermission("plots.commandplot.admin"))p.setOp(false);
            p.sendMessage(main.messages.getColoredString("cbm-leave"));
            cbMode.remove(new UUIDObject(p.getUniqueId()));
        }
	}

	@EventHandler
	public void onPlotDelete(PlotDeleteEvent event){
        //ToDo: clear Scoreboard and SQL
    }
}
