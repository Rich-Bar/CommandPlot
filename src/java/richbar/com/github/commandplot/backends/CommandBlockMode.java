package richbar.com.github.commandplot.backends;

import java.io.File;
import java.util.UUID;

import richbar.com.github.commandplot.CPlugin;
import richbar.com.github.commandplot.caching.BackendType;
import richbar.com.github.commandplot.caching.CacheObject;
import richbar.com.github.commandplot.caching.UndefinedBackend;
import richbar.com.github.commandplot.caching.io.FileCache;
import richbar.com.github.commandplot.caching.objects.UUIDObject;
import richbar.com.github.commandplot.caching.sql.PlayerSQLWrapper;
import richbar.com.github.commandplot.caching.sql.SQLCache;

public class CommandBlockMode<UUIDObject> extends UndefinedBackend<UUIDObject>{
	
	public CommandBlockMode(CPlugin main, BackendType type) {
		if(type.equals(BackendType.FILE)) backend = new FileCache<>(new File(main.getDataFolder().toString() + "/CommandBlockMode.db"), "cbm");
		else backend = new SQLCache<>(main.sqlMan, new PlayerSQLWrapper(), UUID.class);
		
		loadFromBackend();
	}

    @Override
    public boolean contains(CacheObject<UUIDObject> elem) {
        boolean res = backend.contains(elem);
        System.out.println("cbm containes " + res);
        return res;
    }
}
