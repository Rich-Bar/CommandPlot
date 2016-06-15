package richbar.com.github.commandplot;

import java.io.File;
import java.util.UUID;

import richbar.com.github.commandplot.caching.BackendType;
import richbar.com.github.commandplot.caching.CacheBackend;
import richbar.com.github.commandplot.caching.CacheObject;
import richbar.com.github.commandplot.caching.io.FileCache;
import richbar.com.github.commandplot.caching.objects.UUIDObject;
import richbar.com.github.commandplot.caching.sql.PlayerSQLWrapper;
import richbar.com.github.commandplot.caching.sql.SQLCache;

public class CommandBlockMode implements CacheBackend<UUID>{
	
	private CacheBackend<UUID> backend;
	
	public CommandBlockMode(CPlugin main, BackendType type) {
		if(type.equals(BackendType.FILE)) backend = new FileCache<UUID>(new File(main.getDataFolder().toString() + "/CommandBlockMode.db"), "cbm");
		else backend = new SQLCache<UUID>(main.sqlMan, new PlayerSQLWrapper(), UUIDObject.class);
		
		loadFromBackend();
	}

	@Override
	public boolean remove(CacheObject<UUID> elem) {
		return backend.remove(elem);
	}

	@Override
	public boolean addObject(CacheObject<UUID> elem) {
		return backend.addObject(elem);
	}

	@Override
	public boolean contains(CacheObject<UUID> elem) {
		return backend.contains(elem);
	}

	@Override
	public void loadFromBackend() {
		backend.loadFromBackend();
	}
}
