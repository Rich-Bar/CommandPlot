package richbar.com.github.commandplot.caching.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import richbar.com.github.commandplot.caching.CacheBackend;
import richbar.com.github.commandplot.caching.CacheObject;

public class SQLCache<T> implements CacheBackend<T>{

	private List<CacheObject<T>> cached;
	
	SQLManager sqlMan;
	SQLWrapper sqlWrap;
	Class<?> SQLObjRef;
	
	public SQLCache(SQLManager sqlMan, SQLWrapper sqlWrap, Class<?> SQLObjRef) {
		this.sqlMan = sqlMan;
		this.sqlWrap = sqlWrap;
		this.SQLObjRef = SQLObjRef;
		
		loadFromBackend();
	}
	
	public boolean remove(CacheObject<T> elem){
		if(remSQL(elem)) return cached.remove(elem);
		return false;
	}
	
	private boolean remSQL(CacheObject<T> elem){
		ResultSet res = sqlMan.mysqlquery(sqlWrap.getObject(elem));
		if(res == null) return true;
		if(sqlMan.mysqlexecution(sqlWrap.getRemoveObject(elem)))return true;
		System.out.println("WARNING, could not remove Element ["+ sqlWrap.getTypeName() +", "+ elem.toString() +"] from CommandMode Table!");
		return false;
	}
	
	public boolean addObject(CacheObject<T> elem){
		cached.add(elem);
		return sqlMan.mysqlexecution(sqlWrap.getAddObject(elem));
	}
	
	public boolean contains(CacheObject<T> elem){
		return cached.contains(elem);
	}
	
	public void loadFromBackend(){
		ResultSet allObjects = sqlMan.mysqlquery(sqlWrap.getAllObjects());
		if(allObjects == null) return;
		try {
			while(allObjects.next()){
				@SuppressWarnings("unchecked")
				CacheObject<T> sqlObj = ((CacheObject<T>)SQLObjRef.newInstance());
				sqlObj.fromString(allObjects.getString(sqlWrap.getTypeName()));
				cached.add(sqlObj);
			}
		} catch (InstantiationException | IllegalAccessException | SQLException e) {}
	}
}
