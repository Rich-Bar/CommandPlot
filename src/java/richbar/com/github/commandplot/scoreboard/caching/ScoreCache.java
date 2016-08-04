package richbar.com.github.commandplot.scoreboard.caching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.objects.ObjectiveObject;

public class ScoreCache {
	
	private Map<UUID, Map<String, Integer>> scores = new HashMap<>();
	private SQLManager sqlMan;
	
	public ScoreCache(SQLManager man) {
		this.sqlMan = man;
		create();
		loadFromBackend();
	}
	
	public void create(){
		sqlMan.mysqlexecution(ScoreWrapper.getCreateTable());
	}
	
	private void loadFromBackend() {
		ResultSet allObjects = sqlMan.mysqlquery(ScoreWrapper.getAllObjectives());
		if(allObjects == null) return;
		try {
			while(allObjects.next()){
				String plotid = allObjects.getString("plotid");
				UUID uuid = UUID.fromString(allObjects.getString("uuid"));
				String name = allObjects.getString("name");
				int score = allObjects.getInt("score");
				
				Map<String, Integer> newScores = scores.containsKey(uuid)? scores.get(uuid) : new HashMap<>();
				if(newScores == null) newScores = new HashMap<>();
				newScores.put(plotid + "|" + name, score);
				scores.put(uuid, newScores);
			}
		} catch (SQLException e) {}
	}

	/*TODO: Track and Statistic criteria
	 * 
	public void getScore(LivingEntity e){
		if(e instanceof Player){
			Player p = (Player) e;
		}
	}*/
	
	public void setScore(PlotId pId, UUID uuid, ObjectiveObject objective, int val){
		setScore(pId, uuid, objective.name, val);
	}
	
	public void setScore(PlotId pId, UUID uuid, String name, int val){
		getAllScores(uuid).replace(pId.toString() +"|"+ name , val);
		sqlMan.mysqlexecution(ScoreWrapper.getSetSpecificScore(uuid, pId,  name, val));
	}
	
	public void changeScore(PlotId pId, UUID uuid, String name, int by){
		getAllScores(uuid).replace(pId.toString() +"|"+ name , getScore(pId, uuid, name) + by);
		sqlMan.mysqlexecution(ScoreWrapper.getChangeSpecificScore(uuid, pId, name, by));
	}
	
	public int getScore(PlotId pId, UUID uuid, ObjectiveObject objective){
		return getScore(pId, uuid, objective.name);
	}
	
	public int getScore(PlotId pId, UUID uuid, String name){
		return getAllScores(uuid).get(pId.toString() +"|"+ name);
	}
	
	public Set<UUID> getAllPlayers(){
		return scores.keySet();
	}
	
	public Map<String, Integer> getAllScores(UUID uuid){
		Map<String, Integer> map = scores.containsKey(uuid)?scores.get(uuid):new HashMap<>(); 
		if(map == null){
			map = new HashMap<>();
			scores.put(uuid, map);
		}
		return map;
	}
	
	public void removeScore(UUID uuid, PlotId pId, ObjectiveObject objective){
		removeScore(uuid, pId, objective.name);
	}
	
	public void removeScore(UUID uuid, PlotId pId, String name){
		getAllScores(uuid).remove(pId.toString() +"|"+ name);
		sqlMan.mysqlexecution(ScoreWrapper.getRemoveSpecificPlayerObjective(uuid, pId, name));
	}	

	public void removePlayer(UUID uuid){
		scores.remove(uuid);
		sqlMan.mysqlexecution(ScoreWrapper.getRemovePlayerObjectives(uuid));
	}
}
