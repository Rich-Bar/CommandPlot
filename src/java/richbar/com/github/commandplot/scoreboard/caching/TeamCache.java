package richbar.com.github.commandplot.scoreboard.caching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.objects.TeamObject;

public class TeamCache{

	private Map<PlotId, List<TeamObject>> teams = new HashMap<>();
	
	private TeamWrapper wrapper;
	private SQLManager sqlMan;

	public TeamCache(SQLManager man) {
		this.sqlMan = man;
	
		loadFromBackend();
	}

	private void loadFromBackend() {
		@SuppressWarnings("static-access")
		ResultSet allObjects = sqlMan.mysqlquery(wrapper.getAllTeams());
		if(allObjects == null) return;
		try {
			while(allObjects.next()){
				String plotid = allObjects.getString("plotid");
				String name = allObjects.getString("name");
				String displayname = allObjects.getString("displayname");
				int color = allObjects.getInt("color");
				@SuppressWarnings("static-access")
				boolean[] settings = wrapper.getSettingsBoolean(allObjects.getInt("settings"));
				
			}
		} catch (SQLException e) {}
	}
	
	public void addTeam(PlotId pId, TeamObject team){
		List<TeamObject> oldTeams = getAllTeams(pId);
		oldTeams.add(team);
		teams.put(pId, oldTeams);
		//TODO: MYSQL
	}
	
	public List<TeamObject> getAllTeams(PlotId pId){
		List<TeamObject> teamObjs = teams.containsKey(pId)? teams.get(pId): new ArrayList<>();
		if(teamObjs == null){
			teamObjs = new ArrayList<>();
			teams.put(pId, teamObjs);
		}
		return teamObjs;
	}
	
	public TeamObject getTeam(PlotId pId, String name){
		for(TeamObject obj : getAllTeams(pId)){
			if(obj.name == name) return obj;
		}
		return null;
	}
	
	public boolean containsTeam(PlotId pId, String name){
		for(TeamObject obj : getAllTeams(pId)){
			if(obj.name == name) return true;
		}
		return false;
	}
	
	public void removeTeam(PlotId pId, String name){
		for(TeamObject obj : getAllTeams(pId)){
			if(obj.name == name) teams.get(pId).remove(obj);
		}
		//TODO: MYSQL
	}
	
	public void removePlot(PlotId pId){
		teams.remove(pId);
		//TODO: MYSQL
	}
}
