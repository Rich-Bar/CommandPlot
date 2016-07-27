package richbar.com.github.commandplot.caching;

import java.io.Serializable;

@SuppressWarnings("serial")
	public abstract class CacheObject<T> implements Serializable{
		
		protected T object;
		
		public CacheObject(T object) {
			this.object = object;
		}
		
		public CacheObject(String parse){
			this.object = fromString(parse);
		}
		
		public CacheObject() {}
		
		public T getObject(){
			return object;
		}
		
		public abstract String toString();
		public abstract T fromString(String serialized);
		
	}