package richbar.com.github.commandplot;

import com.intellectualcrafters.plot.PS;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import richbar.com.github.commandplot.api.PlotChecker;
import richbar.com.github.commandplot.api.PlotSquaredChecker;
import richbar.com.github.commandplot.backends.ActivePlots;
import richbar.com.github.commandplot.backends.CommandBlockMode;
import richbar.com.github.commandplot.caching.BackendType;
import richbar.com.github.commandplot.caching.sql.PlayerSQLWrapper;
import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.command.CBModeCommand;
import richbar.com.github.commandplot.command.CommandPlotCommand;
import richbar.com.github.commandplot.command.Commands;
import richbar.com.github.commandplot.command.pipeline.MapChanger;
import richbar.com.github.commandplot.command.pipeline.SimpleCommandManager;
import richbar.com.github.commandplot.listener.CommandAccessor;
import richbar.com.github.commandplot.scoreboard.ScoreboardCache;
import richbar.com.github.commandplot.scoreboard.ScoreboardFix;
import richbar.com.github.commandplot.util.CustomConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CPlugin extends JavaPlugin{

	public SQLManager sqlMan;
	public PlotChecker<?> check;
	public ActivePlots activePlots;
	public ExecutionLimiter limiter;
	public FileConfiguration messages;
	public ScoreboardCache scoreboard;
	public ScoreboardFix commandScoreboard;
	
    private MapChanger map;
	private CustomConfig config;
    private CommandAccessor cmdAcc;
    private List<String> whitelist = new ArrayList<>();
    
    protected CommandBlockMode cbMode;
    

	@SuppressWarnings("deprecation")
	@Override
    public void onEnable() {
        PluginManager manager = Bukkit.getServer().getPluginManager();
        
        final Plugin plotsquared = manager.getPlugin("PlotSquared");
        if(plotsquared != null && !plotsquared.isEnabled()) {
            manager.disablePlugin(this);
            return;
        }else
        check = new PlotSquaredChecker(PS.get());

        messages = new CustomConfig(this, "messages.yml").getConfig();
        config = new CustomConfig(this, "config.yml");
        
        List<String> backends = Arrays.asList(config.getConfig().getString("backends.commandblockmode").toLowerCase(),
				config.getConfig().getString("backends.activeplots").toLowerCase());
        
        if(backends.contains("sql")){
	        String host = config.getConfig().getString("connection.host");
	        String schema = config.getConfig().getString("connection.schema");
	        String user = config.getConfig().getString("connection.username");
	        String password = config.getConfig().getString("connection.password");
	        sqlMan = new SQLManager(this, host, schema, user, password);
	        if(!sqlMan.isWorking()){
	        	manager.disablePlugin(this);
	        	return;
	        }
	        sqlMan.mysqlexecution(new PlayerSQLWrapper().getCreateTable());
        }
        
        scoreboard = new ScoreboardCache(sqlMan, check);
		commandScoreboard = new ScoreboardFix(messages, scoreboard);

		cbMode = new CommandBlockMode(this, BackendType.valueOf(backends.get(0).toUpperCase()));
        activePlots = new ActivePlots(this, BackendType.valueOf(backends.get(1).toUpperCase()));
        cmdAcc = new CommandAccessor(this, cbMode);
        manager.registerEvents(cmdAcc, this);


        for(Commands command : Commands.values()){
	        whitelist.add(command.getInst().getCommand().toLowerCase());
	    }
        whitelist.add("reload");
		whitelist.add("scoreboard");
        
        getCommand("commandblockmode").setExecutor(new CBModeCommand(this, cbMode));
        getCommand("commandblock").setExecutor(new CBModeCommand(this, cbMode));
        getCommand("commandplot").setExecutor(new CommandPlotCommand(this));
        
        
        /*
         * You can buy this plugin from me
         * [Skype: MarcoDiRich | bit.do/RichY]
         * this feature won't work if you dont buy it!
         * 
         * it is used for disabling commandblocks
         * from running other commands than specified!
         */
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
        	 public void run() {
        		 try {
					Class.forName("richbar.com.github.commandplot.command.pipeline.SimpleCommandManager");
				    getLogger().info("Overriding Standart executors!..");
	        	} catch (ClassNotFoundException e1) {
					getLogger().info("This is the free Version!");
					getLogger().info("Commandblocks may run anything...");
				}
				map = new MapChanger(getPlugin(CPlugin.class));
			    try {
					map.registerMiddleExecutor();
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException e) {
					 getLogger().warning("Could not Override standart Executors!!!");
					 getLogger().warning("Commandblocks might run commands of Plugins!!!");
					e.printStackTrace();
				}
			}
		}, 100L); 	
        
        limiter = new ExecutionLimiter(this);
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, limiter, 0, 1);
    }

	public List<String> getWhitelist(){
		return whitelist;
	}

	public Command getCommandManager(Command old){
		if(old.getLabel().toLowerCase().contains("scoreboard")) return commandScoreboard;
		if(whitelist.contains(old.getLabel().toLowerCase())) return new CommandManager(this, old);
		return new SimpleCommandManager(old);
	}
	
	
	@Override
	public void onDisable(){
		try{
			map.undo();
		}catch(Exception e1){}
	}
}
