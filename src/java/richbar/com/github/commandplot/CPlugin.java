package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.commands.SetCommand;

import richbar.com.github.commandplot.CommandManager.Commands;
import richbar.com.github.commandplot.command.CBModeCommand;
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
        if(!sqlMan.isWorking()) manager.disablePlugin(this);
        	
        sqlMan.mysqlquery(SQLWrapper.getCreateCommandModeTable());
        
        cbMode = new CommandBlockMode(sqlMan);
        cmdAcc = new CommandAccessor(cbMode);
        manager.registerEvents(cmdAcc, this);
        
		cmdMan = new CommandManager(this);
        for(Commands command : Commands.values()){
	        String cmd = command.getInst().getCommand().toLowerCase();
	        System.out.println("Registering Command " + cmd);
	        whitelist.add(cmd);
	        this.getCommand(cmd).setExecutor(cmdMan);
	    }
        
        getCommand("commandblockmode").setExecutor(new CBModeCommand());
    }

	public List<String> getWhitelist(){
		return whitelist;
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
