package richbar.com.github.commandplot.util;

import java.util.UUID;

public class SQLWrapper {
	
	public static String getCommandModeTable(){
		return "`%SCHEMA%`.`commandmode`";
	}
	
	public static String getAddPlayer(UUID playerID){
		return "INSERT INTO "+ getCommandModeTable() +
				" (`UUID`) VALUES ('"+ playerID +"')";
	}
	
	public static String getRemovePlayer(UUID playerID){
		return "DELETE FROM "+ getCommandModeTable() +" WHERE `UUID` = '"+ playerID +"'";
	}
	
	public static String getCreateCommandModeTable(){
		return "CREATE TABLE "+ getCommandModeTable() +" ( `UUID` VARCHAR(38) NOT NULL, UNIQUE INDEX `UUID_UNIQUE` (`UUID` ASC))";
	}
	
	public static String getPlayer(UUID playerID){
		return "SELECT `UUID` FROM " + getCommandModeTable() + " WHERE `UUID` = '" + playerID + "'";
	}
	
	public static String getAllPlayers(){
		return "SELECT `UUID` FROM " + getCommandModeTable();
	}
}
