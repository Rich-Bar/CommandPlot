package richbar.com.github.commandplot.caching.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import richbar.com.github.commandplot.caching.CacheBackend;
import richbar.com.github.commandplot.caching.CacheObject;

public class FileCache<T> implements CacheBackend<T>{

	List<String> localCache = new ArrayList<String>();
	
	FileConfiguration cacheFile;
	File ioFile;
	
	String sub;
	
	public FileCache(File fileLoc, String subSector) {
		ioFile = fileLoc;
		cacheFile = YamlConfiguration.loadConfiguration(fileLoc);
		sub = subSector;
	}
	
	@Override
	public boolean remove(CacheObject<T> elem) {
		if(localCache.contains(elem.toString())){
			localCache.remove(elem.toString());
			cacheFile.set(sub, localCache);
			try {
				cacheFile.save(ioFile);
			} catch (IOException e){ return false; }
			return true;
		}else return false;
	}

	@Override
	public boolean addObject(CacheObject<T> elem) {
			localCache.add(elem.toString());
			cacheFile.set(sub, localCache);
			try {
				cacheFile.save(ioFile);
			} catch (IOException e){ return false; }
			return true;
	}

	@Override
	public boolean contains(CacheObject<T> elem) {
		return localCache.contains(elem.toString());
	}

	@Override
	public void loadFromBackend() {
		localCache = cacheFile.getStringList(sub);
	}

}
