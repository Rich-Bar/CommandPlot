package richbar.com.github.commandplot;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.BackendType;
import richbar.com.github.commandplot.caching.UndefinedBackend;
import richbar.com.github.commandplot.caching.io.FileCache;
import richbar.com.github.commandplot.caching.objects.PlotObject;
import richbar.com.github.commandplot.caching.sql.ActivePlotSQLWrapper;
import richbar.com.github.commandplot.caching.sql.SQLCache;

public class ActivePlots extends UndefinedBackend<PlotId> implements CommandExecutor{

	private CPlugin main;
	
	public ActivePlots(CPlugin main, BackendType type) {		
		if(type.equals(BackendType.FILE)) backend = new FileCache<PlotId>(new File(main.getDataFolder().toString() + "/Plots.db"), "active");
		else backend = new SQLCache<PlotId>(main.sqlMan, new ActivePlotSQLWrapper(), PlotObject.class);
		
		this.main = main;
		loadFromBackend();
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player) || !(sender.hasPermission("plots.command.alwaysActive")))
		return false;
		
		Player p = (Player) sender;
		PlotObject psql = new PlotObject(((Plot) main.check.getPlot(p.getLocation())).getId());

		boolean changeTo = false;
		 if(args.length <= 1) changeTo = !contains(psql);
		 else if(args[1] == "true") changeTo = true;
		
		if(changeTo) return addObject(psql);
		else return remove(psql);
	}
}
