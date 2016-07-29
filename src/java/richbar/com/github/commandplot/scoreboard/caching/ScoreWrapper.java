package richbar.com.github.commandplot.scoreboard.caching;

import java.util.UUID;

import com.intellectualcrafters.plot.object.PlotId;

public class ScoreWrapper {

	public static String getTableName() {
		return "`%SCHEMA%`.`scores`";
	}
	
	public static String getAddObject(PlotId pId, UUID uuid, String name, int score){
		return "INSERT INTO "+ getTableName() +" (`plotid`, `uuid`, `name`, `score`) VALUES ('"+ pId.toString() +"', '"+ uuid.toString() +"', '"+ name +"', '"+ score +"');";
	}
	
	public static String getCreateTable(){
		return "CREATE TABLE " + getTableName() + " ( `id` LARGEINT NOT NULL AUTO_INCREMENT, `plotid` VARCHAR(13) NOT NULL, `uuid` VARCHAR(38) NOT NULL, `name` VARCHAR(32) NOT NULL, `score` INT NOT NULL DEFAULT 0, PRIMARY KEY (`id`));";
	}
	
	public static String getRemovePlot(PlotId pId){
		return "DELETE FROM "+ getTableName() +" WHERE `plotid` = '"+ pId.toString() +"'";
	}
	
	public static String getRemovePlayerObjectives(UUID uuid){
		return "DELETE FROM "+ getTableName() +" WHERE `uuid` = '"+ uuid.toString() +"'";
	}
	
	public static String getRemoveSpecificPlayerObjective(UUID uuid, String name){
		return "DELETE FROM "+ getTableName() +" WHERE `uuid` = '"+ uuid.toString() +"' AND `name` = '" + name + "'";
	}
	
	public static String getChangeSpecificScore(UUID uuid, String name, int incBy){
		return "UPDATE " + getTableName() + " SET `score` = `score` + " + incBy + " WHERE `uuid` = '" + uuid.toString() + "' AND `name` = '" + name + "'";
	}
	
	public static String getSetSpecificScore(UUID uuid, String name, int to){
		return "UPDATE " + getTableName() + " SET `score` = " + to + " WHERE `uuid` = '" + uuid.toString() + "' AND `name` = '" + name + "'";
	}
	
	public static String getAllObjectives(){
		return "SELECT * FROM " + getTableName();
	}
}
