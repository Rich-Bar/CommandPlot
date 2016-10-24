package richbar.com.github.commandplot.caching;

public interface CacheBackend<T> {
	
	boolean remove(CacheObject<T> elem);
	
	boolean addObject(CacheObject<T> elem);
	
	boolean contains(CacheObject<T> elem);
	
	void loadFromBackend() ;
}
