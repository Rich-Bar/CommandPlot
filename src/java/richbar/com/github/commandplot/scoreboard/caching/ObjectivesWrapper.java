package richbar.com.github.commandplot.scoreboard.caching;

import com.intellectualcrafters.plot.object.PlotId;

class ObjectivesWrapper{

	private static String getTableName() {
		return "`%SCHEMA%`.`objectives`";
	}
	
	static String getCreateTable(){
		return "CREATE TABLE "+ getTableName() +" ( `id` INT NOT NULL AUTO_INCREMENT, `plotid` VARCHAR(13) NOT NULL, `name` VARCHAR(32) NOT NULL, `displayname` VARCHAR(45) NOT NULL, `criteria` VARCHAR(32) NOT NULL, PRIMARY KEY (`id`));";
	}
	
	static String getAddObject(PlotId pId, String name, String displayName, String criteria){
		return "INSERT INTO "+ getTableName() +" (`plotid`, `name`, `displayname`, `criteria`) VALUES ('"+ pId.toString() +"', '"+ name +"', '"+ displayName +"', '"+ criteria +"')";
	}
	
	static String getRemovePlotObjectives(PlotId pId){
		return "DELETE FROM "+ getTableName() +" WHERE `plotid` = '"+ pId.toString() +"'";
	}
	
	static String getRemoveObjective(PlotId pId, String name){
		return "DELETE FROM "+ getTableName() +" WHERE `plotid` = '"+ pId.toString() +"' AND `name` = '"+ name +"'";
	}
	
	static String getAllObjects(){
		return "SELECT * FROM " + getTableName();
	}
}
