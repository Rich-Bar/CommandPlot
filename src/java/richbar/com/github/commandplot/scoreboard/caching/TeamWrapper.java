package richbar.com.github.commandplot.scoreboard.caching;

import com.intellectualcrafters.plot.object.PlotId;

public class TeamWrapper{

	public static String getTableName() {
		return null;
	}
	
	public static String getCreateTable(){
		return "CREATE TABLE "+ getTableName() +" (`id` INT NOT NULL AUTO_INCREMENT, `plotid` VARCHAR(13) NOT NULL, `name` VARCHAR(32) NOT NULL, `displayname` VARCHAR(32) NOT NULL, `color` VARCHAR(16) NULL, `settings` INT NULL, PRIMARY KEY (`id`));";
	}
	
	public static String getAddObject(PlotId pId, String name, String displayName, String color, int settingsByte){
		return "INSERT INTO "+ getTableName() +" (`plotid`, `name`, `displayname`, `color`, `settings`) VALUES ('"+ pId.toString() +"', '"+ name +"', '"+ displayName +"', '"+ color +"', )";
	}
	
	public static String getRemovePlotTeams(PlotId pId){
		return "DELETE FROM "+ getTableName() +" WHERE `plotid` = '"+ pId.toString() +"'";
	}

	public static String getRemoveTeam(PlotId pId, String name){
		return "DELETE FROM "+ getTableName() +" WHERE `plotid` = '"+ pId.toString() +"' AND `name` = '" + name + "'";
	}

	public static String getAllTeams(){
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
	
	public static boolean[] getSettingsBoolean(int i){
		int max = 0, fact = 0;
		while(max<i){
			max += Math.pow(2, fact);
			fact++;
		}
		boolean[] res = new boolean[fact];
		for(int j = fact-1; j >= 0; j--){
			if(i - Math.pow(2, j) >= 0){
				res[j] = true;
				System.out.print("1");
				i -= Math.pow(2, j);
			}else{
				res[j] = false;
				System.out.print("0");
			} 
			
		}
		return res;
	}
}
