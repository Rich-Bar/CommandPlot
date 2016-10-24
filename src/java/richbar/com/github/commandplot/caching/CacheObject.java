package richbar.com.github.commandplot.caching;

import java.io.Serializable;

@SuppressWarnings("serial")
	public abstract class CacheObject<T> implements Serializable{
		
		protected T object;

		protected CacheObject(T object) {
			this.object = object;
		}

		protected CacheObject(String parse){
			this.object = fromString(parse);
		}

		protected CacheObject() {}
		
		public T getObject(){
			return object;
		}
		
		public abstract String toString();
		public abstract T fromString(String serialized);
		
	}