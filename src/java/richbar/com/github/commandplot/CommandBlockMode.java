package richbar.com.github.commandplot;

import java.io.File;
import java.util.UUID;

import richbar.com.github.commandplot.caching.BackendType;
import richbar.com.github.commandplot.caching.UndefinedBackend;
import richbar.com.github.commandplot.caching.io.FileCache;
import richbar.com.github.commandplot.caching.objects.UUIDObject;
import richbar.com.github.commandplot.caching.sql.PlayerSQLWrapper;
import richbar.com.github.commandplot.caching.sql.SQLCache;

public class CommandBlockMode extends UndefinedBackend<UUID>{
	
	public CommandBlockMode(CPlugin main, BackendType type) {
		if(type.equals(BackendType.FILE)) backend = new FileCache<UUID>(new File(main.getDataFolder().toString() + "/CommandBlockMode.db"), "cbm");
		else backend = new SQLCache<UUID>(main.sqlMan, new PlayerSQLWrapper(), UUIDObject.class);
		
		loadFromBackend();
	}
	
}
