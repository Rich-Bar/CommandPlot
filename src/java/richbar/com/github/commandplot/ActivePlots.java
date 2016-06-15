package richbar.com.github.commandplot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.objects.PlotObject;
import richbar.com.github.commandplot.caching.sql.ActivePlotSQLWrapper;
import richbar.com.github.commandplot.caching.sql.SQLCache;
import richbar.com.github.commandplot.caching.sql.SQLManager;

public class ActivePlots extends SQLCache<PlotId> implements CommandExecutor{

	CPlugin main;
	
	public ActivePlots(CPlugin main, SQLManager sqlMan) {
		super(sqlMan, new ActivePlotSQLWrapper(), PlotObject.class);
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player) || !(sender.hasPermission("plots.command.alwaysActive")))
		return false;
		
		Player p = (Player) sender;
		
		boolean changeTo = false;
		if(args[0] == "true") changeTo = true;
		else if(args.length == 0) changeTo = main.check.isInPlot(p);
		
		PlotObject psql = new PlotObject(((Plot) main.check.getPlot(p.getLocation())).getId());
		
		if(changeTo) return addObject(psql);
		else return remove(psql);
	}

}
