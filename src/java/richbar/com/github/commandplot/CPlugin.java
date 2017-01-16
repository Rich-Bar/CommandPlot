package richbar.com.github.commandplot;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import richbar.com.github.commandplot.backends.ActivePlots;
import richbar.com.github.commandplot.backends.CommandBlockMode;
import richbar.com.github.commandplot.caching.BackendType;
import richbar.com.github.commandplot.caching.sql.PlotIdSQLWrapper;
import richbar.com.github.commandplot.caching.sql.PlayerSQLWrapper;
import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.command.CBModeCommand;
import richbar.com.github.commandplot.command.CommandPlotCommand;
import richbar.com.github.commandplot.command.Commands;
import richbar.com.github.commandplot.command.ElemType;
import richbar.com.github.commandplot.command.pipeline.MapChanger;
import richbar.com.github.commandplot.listener.CommandAccessor;
import richbar.com.github.commandplot.listener.CommandPlotListener;
import richbar.com.github.commandplot.scoreboard.ScoreboardCache;
import richbar.com.github.commandplot.scoreboard.ScoreboardFix;
import richbar.com.github.commandplot.util.CustomConfig;

import java.util.*;
import java.util.logging.Level;

public class CPlugin extends JavaPlugin{
	
	public static boolean isDebug = false;
	public SQLManager sqlMan;
	public ActivePlots activePlots;
	public ExecutionLimiter limiter;
	public CustomConfig messages;
	public CommandBlockMode cbMode;
    public Map<String, List<ElemType>> confWhitelist = new HashMap<>();
    public List<String> confBlacklist = new ArrayList<>();
	public List<String> blacklistedMobs;

	
	private final List<String> whitelist = new ArrayList<>();
    private CustomConfig config;
    private MapChanger map;
    
	//SCOREBOARD:
	public ScoreboardCache scoreboard;
    private ScoreboardFix commandScoreboard;
    

	@SuppressWarnings("deprecation")
	@Override
    public void onEnable() {

        PluginManager manager = Bukkit.getServer().getPluginManager();
		CommandPlotListener plotListener;
		CommandAccessor cmdAcc;
        
        final Plugin plotsquared = manager.getPlugin("PlotSquared");
        if(plotsquared != null && !plotsquared.isEnabled()) {
            manager.disablePlugin(this);
            return;
        }else
        messages = new CustomConfig(this, "messages.yml");
        config = new CustomConfig(this, "config.yml");
		config.getConfig().getStringList("mobs.blacklist");
        
        List<String> backends = Arrays.asList(config.getConfig().getString("backends.commandblockmode").toLowerCase(),
				config.getConfig().getString("backends.activeplots").toLowerCase());

        if(isDebug()){
            isDebug = true;
            Bukkit.getLogger().setLevel(Level.ALL);
        }

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
            sqlMan.mysqlexecution(new PlotIdSQLWrapper().getCreateTable());
        }

        //SCOREBOARD:
		scoreboard = new ScoreboardCache(sqlMan);
		commandScoreboard = new ScoreboardFix(messages, scoreboard);

		cbMode = new CommandBlockMode(this, BackendType.valueOf(backends.get(0).toUpperCase()));
        activePlots = new ActivePlots(this, BackendType.valueOf(backends.get(1).toUpperCase()));

		cmdAcc = new CommandAccessor(this, cbMode);
		plotListener = new CommandPlotListener(this, cbMode);
        manager.registerEvents(cmdAcc, this);
		manager.registerEvents(plotListener, this);


        for(Commands command : Commands.values()){
	        whitelist.add(command.getInst().getCommand().toLowerCase());
	    }
        whitelist.add("reload");
        //SCOREBOARD:
		whitelist.add("scoreboard");

        for(String cmd : getConfig().getStringList("commands.whitelist")){
            String label = cmd.contains("-")? cmd.substring(0, cmd.indexOf("-")) :  cmd;
            whitelist.add(label);
            List<ElemType> elems = new ArrayList<>();
            for(String elem : cmd.substring(cmd.indexOf("-") + 1).split(",")){
                elem = elem.trim();
                ElemType type = ElemType.valueOf(elem);
                if(type != null && elem != null) elems.add(type);
            }
            confWhitelist.put(label, elems);
        }
        confBlacklist.addAll(getConfig().getStringList("commands.blacklist"));

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
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            try {
               Class.forName("richbar.com.github.commandplot.command.pipeline.SimpleCommandManager");
               getLogger().info("Overriding Standart executors!..");
                map = new MapChanger(getPlugin(CPlugin.class));
                try {
                    map.registerMiddleExecutor();
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    getLogger().warning("Could not Override standart Executors!!!");
                    getLogger().warning("Commandblocks might run commands of Plugins!!!");
                }
            } catch (ClassNotFoundException e1) {
                getLogger().info("This is the free Version!");
                getLogger().info("Commandblocks may run anything...");
            }
       }, 1L);

        limiter = new ExecutionLimiter(this);
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, limiter, 0, 1);
    }

	public List<String> getWhitelist(){
		return whitelist;
	}

	public boolean isDebug(){
		return (config.getConfig().contains("debug") && config.getConfig().getBoolean("debug"));
	}
	
	@Override
	public void onDisable(){
		try{
			map.undo();
		}catch(Exception ignored){}
	}

	@Override
    public FileConfiguration getConfig(){
        return config.getConfig();
    }
}
