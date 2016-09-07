package richbar.com.github.commandplot.command.pipeline;

import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import richbar.com.github.commandplot.CPlugin;

import java.lang.reflect.Field;
import java.util.Map;

class CustomCommandMap {
	
	private CPlugin main;

	Map<String, Command> knownCommands;
	SimpleCommandMap commandMap;
	
	CustomCommandMap(CPlugin main) {
		this.main = main;
	}
	
	@SuppressWarnings("unchecked")
	void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		PluginManager manager = main.getServer().getPluginManager();
		SimplePluginManager spm = (SimplePluginManager) manager;
		
		Field commandMapField;
		if (spm != null)
		{
		    Field pluginsField = spm.getClass().getDeclaredField("plugins");
		    Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");
		    commandMapField = spm.getClass().getDeclaredField("commandMap");
		    pluginsField.setAccessible(true);
		    lookupNamesField.setAccessible(true);
		    commandMapField.setAccessible(true);
		    
		    commandMap = (SimpleCommandMap) commandMapField.get(spm);
		    
		    Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
		    knownCommandsField.setAccessible(true);
		    knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
		}
	}
}
