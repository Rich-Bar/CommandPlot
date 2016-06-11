package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.intellectualcrafters.plot.PS;

import richbar.com.github.commandplot.CommandManager.Commands;
import richbar.com.github.commandplot.api.PlotChecker;
import richbar.com.github.commandplot.api.PlotSquaredChecker;
import richbar.com.github.commandplot.command.CBModeCommand;
import richbar.com.github.commandplot.command.pipeline.*;
import richbar.com.github.commandplot.sql.PlayerSQLWrapper;
import richbar.com.github.commandplot.sql.caching.SQLManager;
import richbar.com.github.commandplot.util.CustomConfig;

public class CPlugin extends JavaPlugin{

	public SQLManager sqlMan;
	public PlotChecker<?> check;
	public FileConfiguration messages;
	private CustomConfig config;
    public CommandManager cmdMan;
    private CommandAccessor cmdAcc;
    private MapChanger map;
    protected CommandBlockMode cbMode;
    
    private List<String> whitelist = new ArrayList<>();

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
        
        cbMode = new CommandBlockMode(sqlMan);
        cmdAcc = new CommandAccessor(this, cbMode);
        manager.registerEvents(cmdAcc, this);
        
        for(Commands command : Commands.values()){
	        whitelist.add(command.id.getCommand().toLowerCase());
	    }
        
        getCommand("commandblockmode").setExecutor(new CBModeCommand(cbMode));
        getCommand("commandblock").setExecutor(new CBModeCommand(cbMode));
        //getCommand("transmit").setExecutor((CustomCommand) Commands.TRANSMIT.id);
        //getCommand("receive").setExecutor((CustomCommand) Commands.TRANSMIT.id);
        //getCommand("alwaysActive").setExecutor((CustomCommand) Commands.TRANSMIT.id);
        
        
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
    }

	public List<String> getWhitelist(){
		return whitelist;
	}
	
	
	
	@Override
	public void onDisable(){
		try{
			map.undo();
		}catch(Exception e1){}
	}
}
