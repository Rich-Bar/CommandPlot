package richbar.com.github.commandplot.scoreboard.caching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.objects.TeamObject;

public class TeamCache{

	private Map<PlotId, List<TeamObject>> teams = new HashMap<>();
	
	private SQLManager sqlMan;

	public TeamCache(SQLManager man) {
		this.sqlMan = man;
		create();
		loadFromBackend();
	}

	private void loadFromBackend() {
		ResultSet allObjects = sqlMan.mysqlquery(TeamWrapper.getAllTeams());
		if(allObjects == null) return;
		try {
			while(allObjects.next()){
				String[] plotid = allObjects.getString("plotid").split(";");
				String name = allObjects.getString("name");
				String displayname = allObjects.getString("displayname");
				String color = allObjects.getString("color");
				boolean[] settings = TeamWrapper.getSettingsBoolean(allObjects.getInt("settings"));
				
				PlotId pId = new PlotId(Integer.parseInt(plotid[0]), Integer.parseInt(plotid[1]));
				if(!teams.containsKey(pId)) teams.put(pId, new ArrayList<>());
				List<TeamObject> list = teams.get(pId);
				list.add(new TeamObject(pId, name, displayname, color, settings));
			}
		} catch (SQLException e) {}
	}
	
	public void addTeam(PlotId pId, TeamObject team){
		List<TeamObject> oldTeams = getAllTeams(pId);
		oldTeams.add(team);
		teams.put(pId, oldTeams);
		sqlMan.mysqlexecution(TeamWrapper.getAddObject(pId, team.name, team.displayName, team.color, TeamWrapper.getSettingsInt(team.allowFriendlyFire, team.collisionOwnTeam, team.collissionOtherTeams, team.deathMessageOtherTeams, team.deathMessageOwnTeam, team.nameTagsOtherTeam, team.nameTagsOwnTeam, team.SeeFriendlyInvisibles)));
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
		sqlMan.mysqlexecution(TeamWrapper.getRemoveTeam(pId, name));
	}
	
	public void removePlot(PlotId pId){
		teams.remove(pId);
		sqlMan.mysqlexecution(TeamWrapper.getRemovePlotTeams(pId));
	}
	
	public void create(){
		sqlMan.mysqlexecution(TeamWrapper.getCreateTable());
	}
	
	enum TeamColors{
		black, dark_blue, dark_green, dark_aqua, dark_red, dark_purple, gold, gray, dark_gray, blue, green, aqua, red, light_purple, yellow, white;
	}
}
