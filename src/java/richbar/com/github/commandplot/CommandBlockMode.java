package richbar.com.github.commandplot;

import java.util.UUID;

import richbar.com.github.commandplot.sql.PlayerSQLWrapper;
import richbar.com.github.commandplot.sql.caching.SQLCache;
import richbar.com.github.commandplot.sql.caching.SQLManager;
import richbar.com.github.commandplot.sql.caching.SQLObject;

public class CommandBlockMode extends SQLCache<UUID>{
	
	public CommandBlockMode(SQLManager sqlMan) {
		super(sqlMan, new PlayerSQLWrapper(), UUIDSQL.class);
	}
	
	public boolean removePlayer(UUID pID){
		return remove(new UUIDSQL(pID));
	}
	
	public boolean addPlayer(UUID pID){
		return addObject(new UUIDSQL(pID));
	}
	
	public boolean isActive(UUID pID){
		return contains(new UUIDSQL(pID));
	}
	
	@SuppressWarnings("serial")
	public class UUIDSQL extends SQLObject<UUID>{

		public UUIDSQL(UUID uuid) {
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
}
