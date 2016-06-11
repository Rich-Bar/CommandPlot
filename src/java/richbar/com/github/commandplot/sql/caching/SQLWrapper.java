package richbar.com.github.commandplot.sql.caching;

public abstract class SQLWrapper {
	
	public abstract String getTableName();
	public abstract String getTypeName();
	public abstract int getVarCharLimit();
	public abstract boolean hasPrimary();
	
	public String getAddObject(SQLObject<?> object){
		return "INSERT INTO "+ getTableName() +" (`"+ getTypeName() +"`) VALUES ('"+ object.toString() +"')";
	}
	
	public String getRemoveObject(SQLObject<?> object){
		return "DELETE FROM "+ getTableName() +" WHERE `"+ getTypeName() +"` = '"+ object.toString() +"'";
	}
	
	public String getCreateTable(){
		return "CREATE TABLE "+ getTableName() +" ( `"+ getTypeName() +"` VARCHAR("+ getVarCharLimit() +") NOT NULL, UNIQUE INDEX `"+ getTypeName() +"_UNIQUE` (`"+ getTypeName() +"` ASC))";
	}
	
	public String getObject(SQLObject<?> object){
		return "SELECT `"+ getTypeName() +"` FROM " + getTableName() + " WHERE `"+ getTypeName() +"` = '" + object.toString() + "'";
	}
	
	public String getAllObjects(){
		return "SELECT `"+ getTypeName() +"` FROM " + getTableName();
	}
}
