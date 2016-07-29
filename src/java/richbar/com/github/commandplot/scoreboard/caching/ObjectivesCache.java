package richbar.com.github.commandplot.scoreboard.caching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.objects.ObjectiveObject;

public class ObjectivesCache{

	private Map<PlotId, List<ObjectiveObject>> plotObjectives = new HashMap<>();
	
	protected SQLManager sqlMan;
	protected ObjectivesWrapper sqlWrap;
	
	public ObjectivesCache(SQLManager sqlMan) {
		this.sqlMan = sqlMan;
		
		loadFromBackend();
	}
	
	public void addObjective(PlotId pId, ObjectiveObject objective){
		List<ObjectiveObject> obj = getAllObjectives(pId);
		if(obj == null || obj.isEmpty()) obj = new ArrayList<>();
		obj.add(objective);
		plotObjectives.put(pId, obj);
		//TODO: MYSQL
	}
	
	public ObjectiveObject getObjectiveByName(PlotId pId, String name){
		for(ObjectiveObject obj : getAllObjectives(pId)){
			if(obj.name.equalsIgnoreCase(name))return obj;
		}
		return null;
	}
	
	public List<ObjectiveObject> getObjectivesByCriteria(PlotId pId, String criteria){
		List<ObjectiveObject> res = new ArrayList<>();
		for(ObjectiveObject obj : getAllObjectives(pId)){
			if(obj.criteria.equalsIgnoreCase(criteria))res.add(obj);
		}
		return res;
	}
	
	public List<ObjectiveObject> getAllObjectives(PlotId pId){
		List<ObjectiveObject> objs = plotObjectives.containsKey(pId)? plotObjectives.get(pId) : new ArrayList<>();
		if(objs == null){
			objs = new ArrayList<>();
			plotObjectives.put(pId, objs);
		}
		return objs;
	}
	
	public boolean contains(PlotId pId, ObjectiveObject objective){
		return getAllObjectives(pId).contains(objective);
	}
	
	public boolean contains(PlotId pId, String objective){
		for(ObjectiveObject obj : getAllObjectives(pId)){
			if(obj.name == objective) return true;
		}
		return false;
	}
	
	public void removeObjective(PlotId pId, ObjectiveObject objective){
		if(getAllObjectives(pId).isEmpty()) return;
		plotObjectives.get(pId).remove(objective);
		//TODO: MYSQL
	}

	public void removeObjective(PlotId pId, String objective){
		for(ObjectiveObject obj : getAllObjectives(pId)){
			if(obj.name == objective) plotObjectives.get(pId).remove(objective);
			//TODO: MYSQL
		}
	}
	
	public void removePlot(PlotId pId){
		if(plotObjectives.containsKey(pId))plotObjectives.remove(pId);
		//TODO: MYSQL
	}
	
	public void loadFromBackend(){
		@SuppressWarnings("static-access")
		ResultSet allObjects = sqlMan.mysqlquery(sqlWrap.getAllObjects());
		if(allObjects == null) return;
		try {
			while(allObjects.next()){
				String[] plotid = allObjects.getString("plotid").split(";");
				String name = allObjects.getString("name");
				String displayName = allObjects.getString("displayname");
				String criteria = allObjects.getString("criteria");
				
				PlotId pId = new PlotId(Integer.parseInt(plotid[0]), Integer.parseInt(plotid[1]));
				if(!plotObjectives.containsKey(pId)) plotObjectives.put(pId, new ArrayList<>());
				List<ObjectiveObject> list = plotObjectives.get(pId);
				list.add(new ObjectiveObject(pId, name, displayName, criteria));
				plotObjectives.put(pId, list);
			}
		} catch (SQLException e) {}
	}
}
