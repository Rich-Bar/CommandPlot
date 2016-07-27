package richbar.com.github.commandplot.scoreboard.caching;

import com.intellectualcrafters.plot.object.PlotId;

public class ObjectivesWrapper{

	public String getTableName() {
		return "`%SCHEMA%`.`objectives`";
	}
	
	public String getCreateTable(){
		return "CREATE TABLE "+ getTableName() +" ( `id` INT NOT NULL AUTO_INCREMENT, `plotid` VARCHAR(13) NOT NULL, `name` VARCHAR(32) NOT NULL, `displayname` VARCHAR(45) NOT NULL, `criteria` VARCHAR(32) NOT NULL, PRIMARY KEY (`id`));";
	}
	
	public String getAddObject(PlotId pId, String name, String displayName, String criteria){
		return "INSERT INTO "+ getTableName() +" (`plotid`, `name`, `displayname`, `criteria`) VALUES ('"+ pId.toString() +"', '"+ name +"', '"+ displayName +"', '"+ criteria +"')";
	}
	
	public String getRemovePlotObjectives(PlotId pId){
		return "DELETE FROM "+ getTableName() +" WHERE `plotid` = '"+ pId.toString() +"'";
	}
	
	public String getRemoveObjective(String name){
		return "DELETE FROM "+ getTableName() +" WHERE `name` = '"+ name +"'";
	}
	
	public String getPlotObjectives(PlotId pId){
		return "SELECT * FROM " + getTableName() + " WHERE `plotid` = '" + pId.toString() + "'";
	}
	
	public String getObjective(String name){
		return "SELECT * FROM " + getTableName() + " WHERE `plotid` = '" + name + "'";
	}
	
	public String getAllObjects(){
		return "SELECT * FROM " + getTableName();
	}
}
