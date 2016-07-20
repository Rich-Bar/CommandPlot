package richbar.com.github.commandplot.caching;

public abstract class UndefinedBackend<T> implements CacheBackend<T>{

	protected CacheBackend<T> backend;

	@Override
	public boolean remove(CacheObject<T> elem) {
		return backend.remove(elem);
	}

	@Override
	public boolean addObject(CacheObject<T> elem) {
		return backend.addObject(elem);
	}

	@Override
	public boolean contains(CacheObject<T> elem) {
		return backend.contains(elem);
	}

	@Override
	public void loadFromBackend() {
		backend.loadFromBackend();
	}
}
