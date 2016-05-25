package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.intellectualcrafters.plot.api.PlotAPI;

import richbar.com.github.commandplot.CommandManager.Commands;
import richbar.com.github.commandplot.command.CBModeCommand;
import richbar.com.github.commandplot.command.pipeline.*;
import richbar.com.github.commandplot.util.CustomConfig;
import richbar.com.github.commandplot.util.PlotAPIChecker;
import richbar.com.github.commandplot.util.SQLManager;
import richbar.com.github.commandplot.util.SQLWrapper;

public class CPlugin extends JavaPlugin{

	public SQLManager sqlMan;
	public PlotAPIChecker check;
	private CustomConfig config;
    private CommandManager cmdMan;
    private CommandAccessor cmdAcc;
    protected CommandBlockMode cbMode;
    
    private List<String> whitelist = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@Override
    public void onEnable() {
		
        PluginManager manager = Bukkit.getServer().getPluginManager();
        
        final Plugin plotsquared = manager.getPlugin("PlotSquared");
        if(plotsquared != null && !plotsquared.isEnabled()) {
            manager.disablePlugin(this);
            return;
        }
        check = new PlotAPIChecker(new PlotAPI(this));
        
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
        	
        sqlMan.mysqlquery(SQLWrapper.getCreateCommandModeTable());
        
        cbMode = new CommandBlockMode(sqlMan);
        cmdAcc = new CommandAccessor(cbMode);
        manager.registerEvents(cmdAcc, this);
        
        for(Commands command : Commands.values()){
	        String cmd = command.getInst().getCommand().toLowerCase();
	        PluginCommand pluginCommand = this.getCommand(cmd);
	        getLogger().info("Registering Command " + cmd);
	        
	        whitelist.add(cmd.toLowerCase());
	        
	        if(!config.getConfig().contains("commands."+ cmd))
	        	config.getConfig().addDefault("commands."+ cmd, "plots.commandblock.cb");
	        String permission = config.getConfig().getString("commands." + cmd);
	        permission = !(permission == null)? permission : "plots.commandblock.cb";
	        pluginCommand.setPermission(permission);
	        
	        pluginCommand.setExecutor(cmdMan);
	    }
        config.saveConfig();
    	config.reloadConfig();
        
        getCommand("commandblockmode").setExecutor(new CBModeCommand(cbMode));
        getCommand("commandblock").setExecutor(new CBModeCommand(cbMode));
        /*
         * You can buy this plugin from me
         * [Skype: MarcoDiRich | bit.do/RichY]
         * this feature won't work if you dont buy it!
         * 
         * it is used for disabling commandblocks
         * from running other commands than specified!
         */
        	try {
				Class.forName("richbar.com.github.commandplot.command.pipeline.MapChanger");
		        
				MapChanger map = new MapChanger(this);
		        try {
					map.registerMiddleExecutor();
				} catch (NoSuchFieldException | SecurityException
						| IllegalArgumentException | IllegalAccessException e) {
					 getLogger().warning("Could not Override standart Executors!!!");
					 getLogger().warning("Commandblocks might run commands of Plugins!!!");
					e.printStackTrace();
				}
        	} catch (ClassNotFoundException e1) {
				getLogger().info("This is the free Version!");
				getLogger().info("Commandblocks may run anything...");
			}
    }

	public List<String> getWhitelist(){
		return whitelist;
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
