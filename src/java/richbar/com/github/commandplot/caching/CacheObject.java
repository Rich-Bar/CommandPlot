package richbar.com.github.commandplot.caching;

import java.io.Serializable;

@SuppressWarnings("serial")
	public abstract class CacheObject<T> implements Serializable{
		
		public T object;
		
		public CacheObject(T object) {
			this.object = object;
		}
		
		public CacheObject() {}
		
		public abstract String toString();
		public abstract T fromString(String serialized);
		
	}