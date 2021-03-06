package richbar.com.github.commandplot.backends;

import java.io.File;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.flag.Flags;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.CPlugin;
import richbar.com.github.commandplot.CommandRouter;
import richbar.com.github.commandplot.caching.BackendType;
import richbar.com.github.commandplot.caching.UndefinedBackend;
import richbar.com.github.commandplot.caching.io.FileCache;
import richbar.com.github.commandplot.caching.objects.PlotObject;
import richbar.com.github.commandplot.caching.sql.PlotIdSQLWrapper;
import richbar.com.github.commandplot.caching.sql.SQLCache;

public class ActivePlots extends UndefinedBackend<PlotId> implements CommandExecutor{

	private final CPlugin main;
	private final CommandRouter.PSChecker api;
	
	public ActivePlots(CPlugin main, BackendType type) {		
		if(type.equals(BackendType.FILE)) backend = new FileCache<>(new File(main.getDataFolder().toString() + "/Plots.db"), "active");
		else backend = new SQLCache<>(main.sqlMan, new PlotIdSQLWrapper(), PlotObject.class);
		
		this.main = main;
		api = new CommandRouter.PSChecker();
		loadFromBackend();
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player) || !(sender.hasPermission("plots.commandplot.admin")))
		return false;
		
		Player p = (Player) sender;
		PlotObject psql = new PlotObject(api.getPlot(p.getLocation()).getId());

		boolean changeTo = false;
		 if(args.length <= 1) changeTo = !contains(psql);
		 else if(args[1].equals("true")) changeTo = true;
		
		if(changeTo){
			Plot plot = api.getPlot(p.getLocation());
			plot.setFlag(Flags.BLOCKED_CMDS, Arrays.asList("plots", "p", "plot", "ps", "plotsquared", "p2", "2"));
			return addObject(psql);
		}
		else{
			return remove(psql);
		}
	}
}
