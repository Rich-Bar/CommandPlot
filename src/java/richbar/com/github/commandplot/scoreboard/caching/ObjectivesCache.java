package richbar.com.github.commandplot.scoreboard.caching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.LivingEntity;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.CacheObject;
import richbar.com.github.commandplot.caching.sql.SQLCache;
import richbar.com.github.commandplot.caching.sql.SQLManager;
import richbar.com.github.commandplot.caching.sql.SQLWrapper;
import richbar.com.github.commandplot.scoreboard.objects.ObjectiveObject;

public class ObjectivesCache{

	private Map<LivingEntity, List<Map<ObjectiveObject, Integer>>> scores = new HashMap<>();
	private Map<PlotId, List<ObjectiveObject>> plotObjectives = new HashMap<>();
	
	protected SQLManager sqlMan;
	protected ObjectivesWrapper sqlWrap;
	
	public ObjectivesCache(SQLManager sqlMan, ObjectivesWrapper sqlWrap) {
		this.sqlMan = sqlMan;
		this.sqlWrap = sqlWrap;
		
		loadFromBackend();
	}
	
	public boolean remove(PlotId pId, ObjectiveObject elem){
		if(remSQL(elem)) return plotObjectives.get(pId).remove(elem);
		return false;
	}
	
	protected boolean remSQL(PlotId pId, ObjectiveObject elem){
		ResultSet res = sqlMan.mysqlquery(/*ToDo - GetObj*/);
		if(res == null) return true;
		if(sqlMan.mysqlexecution(sqlWrap.getRemoveObject(/*ToDo - RemObj*/)))return true;
		return false;
	}
	
	public boolean addObject(PlotId pId, ObjectiveObject elem){
		List<ObjectiveObject> objectives = plotObjectives.get(pId);
		if(objectives == null) objectives = new ArrayList<>();
		if(!objectives.contains(elem))objectives.add(elem);
		plotObjectives.put(pId, objectives);
		
		return sqlMan.mysqlexecution(/*ToDo - AddObj*/);
	}
	
	public boolean contains(PlotId pId, ObjectiveObject elem){
		List<ObjectiveObject> objectives = plotObjectives.get(pId);
		return objectives == null? false : objectives.contains(elem);
	}
	
	public void loadFromBackend(){
		ResultSet allObjects = sqlMan.mysqlquery(sqlWrap.getAllObjects());
		if(allObjects == null) return;
		try {
			while(allObjects.next()){
				ObjectiveObject elem = new ObjectiveObject(allObjects.getString(sqlWrap.getTypeName()));
				
				List<ObjectiveObject> objectives = plotObjectives.get(pId);
				if(objectives == null) objectives = new ArrayList<>();
				if(!objectives.contains(elem))objectives.add(elem);
				plotObjectives.put(pId, objectives);
			}
		} catch (InstantiationException | IllegalAccessException | SQLException e) {}
	}
}
