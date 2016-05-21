package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import richbar.com.github.commandplot.util.SQLManager;
import richbar.com.github.commandplot.util.SQLWrapper;

public class CommandBlockMode {

	private List<UUID> activePlayers = new ArrayList<>();
	private SQLManager sqlMan;
	
	public CommandBlockMode(SQLManager sqlMan) {
		this.sqlMan = sqlMan;
		loadFromDB();
	}
	
	public boolean removePlayer(UUID pID){
		if(remPlayer(pID)) return activePlayers.remove(pID);
		return false;
	}
	
	private boolean remPlayer(UUID pID){
		List<String> res = sqlMan.mysqlqueryUUID(SQLWrapper.getPlayer(pID));
		if(res == null) return true;
		if(sqlMan.mysqlquery(SQLWrapper.getRemovePlayer(pID)))return true;
		System.out.println("WARNING, could not remove Player[" + pID + "] from CommandMode Table!");
		return false;
	}
	
	public boolean addPlayer(UUID pID){
		activePlayers.add(pID);
		return sqlMan.mysqlquery(SQLWrapper.getAddPlayer(pID));
	}
	
	public boolean isActive(UUID pID){
		return activePlayers.contains(pID);
	}
	
	public void loadFromDB(){
		for(String uid : sqlMan.mysqlqueryUUID(SQLWrapper.getAllPlayers())){
			UUID uuid = UUID.fromString(uid);
			if(!activePlayers.contains(uuid)) activePlayers.add(uuid);
		}
	}
}
