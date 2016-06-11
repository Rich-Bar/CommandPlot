package richbar.com.github.commandplot.sql.caching;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public class SQLManager {
	
	String sqlHost, sqlSchema, sqlUser, sqlPassword;
	Logger logger;
	
	public SQLManager(Plugin plugin, String host, String schema, String user, String password) {
		logger = plugin.getLogger();
		sqlHost = host;
    	sqlHost = sqlHost.charAt(sqlHost.length() -1) == '/'? sqlHost : sqlHost + "/";
		sqlSchema = schema;
		sqlUser = user;
		sqlPassword = password;
	}

	public boolean isWorking(){
		try {
			Connection conn = DriverManager.getConnection(sqlHost + sqlSchema, sqlUser, sqlPassword);
			conn.close();
			return true;
		} catch (SQLException e) {
			logger.warning("SQL Manager - Database connection is invalid!");
			logger.warning("Tried with args: " + sqlHost + sqlSchema + " " + sqlUser + " " + sqlPassword);
			return false;
		}
	}
	
    public boolean mysqlexecution(String query){ 
    	query = query.replace("%SCHEMA%", sqlSchema);
    	try{
    		Connection conn = DriverManager.getConnection(sqlHost + sqlSchema, sqlUser, sqlPassword);
            PreparedStatement sampleQueryStatement = conn.prepareStatement(query); 
            sampleQueryStatement.executeUpdate();
            sampleQueryStatement.close();
            conn.close();
            return true;
    	}catch (SQLException e) {
    		logger.warning("Could not execute SQL update! [" + query + "]");
    		return false;
		}
    }

    public ResultSet mysqlquery(String query){ 
    	query = query.replace("%SCHEMA%", sqlSchema);
    	try{
    		Connection conn = DriverManager.getConnection(sqlHost + sqlSchema, sqlUser, sqlPassword);
            PreparedStatement sampleQueryStatement = conn.prepareStatement(query); 
            ResultSet set = sampleQueryStatement.executeQuery();
            conn.close();
            return set;
    	}catch (SQLException e) {
    		logger.warning("Could not execute SQL query! [" + query + "]");
    		return null;
		}
    }
}
