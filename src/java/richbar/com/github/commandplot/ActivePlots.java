package richbar.com.github.commandplot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.sql.ActivePlotSQLWrapper;
import richbar.com.github.commandplot.sql.caching.SQLCache;
import richbar.com.github.commandplot.sql.caching.SQLManager;
import richbar.com.github.commandplot.sql.caching.SQLObject;

public class ActivePlots extends SQLCache<PlotId> implements CommandExecutor{

	CPlugin main;
	
	public ActivePlots(CPlugin main, SQLManager sqlMan) {
		super(sqlMan, new ActivePlotSQLWrapper(), PlotSQLObject.class);
		this.main = main;
	}
	
	@SuppressWarnings("serial")
	class PlotSQLObject extends SQLObject<PlotId>{

		public PlotSQLObject(PlotId id) {
			super(id);
		}

		@Override
		public String toString() {
			return object.toString();
		}

		@Override
		public PlotId fromString(String serialized) {
			object = PlotId.fromString(serialized);
			return object;
		}
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player) || !(sender.hasPermission("plots.command.alwaysActive")))
		return false;
		
		Player p = (Player) sender;
		
		boolean changeTo = false;
		if(args[0] == "true") changeTo = true;
		else if(args.length == 0) changeTo = main.check.isInPlot(p);
		
		PlotSQLObject psql = new PlotSQLObject(((Plot) main.check.getPlot(p.getLocation())).getId());
		
		if(changeTo) return addObject(psql);
		else return remove(psql);
	}

}
