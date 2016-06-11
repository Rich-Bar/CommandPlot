package richbar.com.github.commandplot.sql.caching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLCache<T> {

	private List<SQLObject<T>> cached;
	
	SQLManager sqlMan;
	SQLWrapper sqlWrap;
	Class<?> SQLObjRef;
	
	public SQLCache(SQLManager sqlMan, SQLWrapper sqlWrap, Class<?> SQLObjRef) {
		this.sqlMan = sqlMan;
		this.sqlWrap = sqlWrap;
		this.SQLObjRef = SQLObjRef;
		
		try {
			loadFromDB();
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean remove(SQLObject<T> elem){
		if(remSQL(elem)) return cached.remove(elem);
		return false;
	}
	
	private boolean remSQL(SQLObject<T> elem){
		ResultSet res = sqlMan.mysqlquery(sqlWrap.getObject(elem));
		if(res == null) return true;
		if(sqlMan.mysqlexecution(sqlWrap.getRemoveObject(elem)))return true;
		System.out.println("WARNING, could not remove Element ["+ sqlWrap.getTypeName() +", "+ elem.toString() +"] from CommandMode Table!");
		return false;
	}
	
	public boolean addObject(SQLObject<T> elem){
		cached.add(elem);
		return sqlMan.mysqlexecution(sqlWrap.getAddObject(elem));
	}
	
	public boolean contains(SQLObject<T> elem){
		return cached.contains(elem);
	}
	
	public void loadFromDB() throws InstantiationException, IllegalAccessException, SQLException{
		ResultSet allObjects = sqlMan.mysqlquery(sqlWrap.getAllObjects());
		if(allObjects == null) return;
		while(allObjects.next()){
			@SuppressWarnings("unchecked")
			SQLObject<T> sqlObj = ((SQLObject<T>)SQLObjRef.newInstance());
			sqlObj.fromString(allObjects.getString(sqlWrap.getTypeName()));
			cached.add(sqlObj);
		}
	}
}
