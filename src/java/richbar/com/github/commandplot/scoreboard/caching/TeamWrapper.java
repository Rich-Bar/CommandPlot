package richbar.com.github.commandplot.scoreboard.caching;

import com.intellectualcrafters.plot.object.PlotId;

public class TeamWrapper{

	public String getTableName() {
		return null;
	}
	
	public String getCreateTable(){
		return "CREATE TABLE "+ getTableName() +" (`id` INT NOT NULL AUTO_INCREMENT, `plotid` VARCHAR(13) NOT NULL, `name` VARCHAR(32) NOT NULL, `displayname` VARCHAR(32) NOT NULL, `color` VARCHAR(16) NULL, `settings` INT NULL, PRIMARY KEY (`id`));";
	}
	
	public String getAddObject(PlotId pId, String name, String displayName, String colorBits, int settingsByte){
		return "INSERT INTO "+ getTableName() +" (`plotid`, `name`, `displayname`, `color`, `settings`) VALUES ('"+ pId.toString() +"', '"+ name +"', '"+ displayName +"', '"+ colorBits +"', )";
	}
	
	public String getRemovePlotTeams(PlotId pId){
		return "DELETE FROM "+ getTableName() +" WHERE `plotid` = '"+ pId.toString() +"'";
	}

	public String getRemoveTeam(PlotId pId, String name){
		return "DELETE FROM "+ getTableName() +" WHERE `plotid` = '"+ pId.toString() +"' AND `name` = '" + name + "'";
	}

	public String getPlotTeams(PlotId pId){
		return "SELECT * FROM " + getTableName() + " WHERE `plotid` = '" + pId.toString() + "'";
	}
	
	public String getTeam(PlotId pId, String name){
		return "SELECT * FROM " + getTableName() + " WHERE `plotid` = '" + pId.toString() + "' AND `name` = '" + name + "'";
	}
	
	public String getAllTeams(){
		return "SELECT * FROM " + getTableName();
	}
	
	public static int getSettingsInt(boolean... bs){
		int res = 0, fact = 0;
		for(boolean b : bs){
			res += b? Math.pow(2, fact) : 0;
			fact++;
		}
		return res;
	}
}
