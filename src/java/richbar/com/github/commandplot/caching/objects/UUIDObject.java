package richbar.com.github.commandplot.caching.objects;

import java.util.UUID;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
	public class UUIDObject extends CacheObject<UUID>{

		public UUIDObject(UUID uuid) {
			object = uuid;
		}
		
		@Override
		public String toString() {
			return object.toString();
		}

		@Override
		public UUID fromString(String serialized) {
			object = UUID.fromString(serialized);
			return object;
		}
		
	}