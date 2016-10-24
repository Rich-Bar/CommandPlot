package richbar.com.github.commandplot.scoreboard.caching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.scoreboard.objects.ObjectiveObject;

public class ObjectivesCache{

	private final Map<PlotId, List<ObjectiveObject>> plotObjectives = new HashMap<>();

	private final SQLManager sqlMan;
	
	public ObjectivesCache(SQLManager sqlMan) {
		this.sqlMan = sqlMan;
		create();
		loadFromBackend();
	}
	
	public void addObjective(PlotId pId, ObjectiveObject objective){
		List<ObjectiveObject> obj = getAllObjectives(pId);
		if(obj == null || obj.isEmpty()) obj = new ArrayList<>();
		obj.add(objective);
		plotObjectives.put(pId, obj);
		sqlMan.mysqlexecution(ObjectivesWrapper.getAddObject(pId, objective.name, objective.displayName, objective.criteria));
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
			if(Objects.equals(obj.name, objective)) return true;
		}
		return false;
	}
	
	public void removeObjective(PlotId pId, ObjectiveObject objective){
		if(getAllObjectives(pId).isEmpty()) return;
		plotObjectives.get(pId).remove(objective);
		sqlMan.mysqlexecution(ObjectivesWrapper.getRemoveObjective(pId, objective.name));
	}

	public void removeObjective(PlotId pId, String objective){
		for(ObjectiveObject obj : getAllObjectives(pId)){
			if(Objects.equals(obj.name, objective)) plotObjectives.get(pId).remove(objective);
			sqlMan.mysqlexecution(ObjectivesWrapper.getRemoveObjective(pId, objective));
		}
	}
	
	public void removePlot(PlotId pId){
		if(plotObjectives.containsKey(pId))plotObjectives.remove(pId);
		sqlMan.mysqlexecution(ObjectivesWrapper.getRemovePlotObjectives(pId));
	}

	private void create(){
		sqlMan.mysqlexecution(ObjectivesWrapper.getCreateTable());
	}

	private void loadFromBackend(){
		ResultSet allObjects = sqlMan.mysqlquery(ObjectivesWrapper.getAllObjects());
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
		} catch (SQLException ignored) {}
	}
}
