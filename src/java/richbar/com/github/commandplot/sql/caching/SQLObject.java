package richbar.com.github.commandplot.sql.caching;

import java.io.Serializable;

@SuppressWarnings("serial")
	public abstract class SQLObject<T> implements Serializable{
		
		public T object;
		
		public SQLObject(T object) {
			this.object = object;
		}
		
		public SQLObject() {}
		
		public abstract String toString();
		public abstract T fromString(String serialized);
		
	}