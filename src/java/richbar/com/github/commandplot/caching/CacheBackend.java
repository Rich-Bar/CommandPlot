package richbar.com.github.commandplot.caching;

public interface CacheBackend<T> {
	
	public boolean remove(CacheObject<T> elem);
	
	public boolean addObject(CacheObject<T> elem);
	
	public boolean contains(CacheObject<T> elem);
	
	public void loadFromBackend() ;
}
