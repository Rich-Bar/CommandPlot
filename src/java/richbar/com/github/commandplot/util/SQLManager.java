package richbar.com.github.commandplot.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public class SQLManager {
	
	String sqlHost, sqlUser, sqlPassword;
	Logger logger;
	
	public SQLManager(Plugin plugin, String host, String user, String password) {
		logger = plugin.getLogger();
		sqlHost = host;
		sqlUser = user;
		sqlPassword = password;
	}

    public void mysqlquery(String query){ 
    	try{
    		Connection conn = DriverManager.getConnection(sqlHost, sqlUser, sqlPassword);
            PreparedStatement sampleQueryStatement = conn.prepareStatement(query); 
            sampleQueryStatement.executeUpdate();
            sampleQueryStatement.close();
            conn.close();
    	}catch (SQLException e) {
    		logger.warning("Could not execute SQL update! [" + query + "]");
		}
    }

    public ResultSet mysqlqueryR(String query){ 
    	try{
    		Connection conn = DriverManager.getConnection(sqlHost, sqlUser, sqlPassword);
            PreparedStatement sampleQueryStatement = conn.prepareStatement(query); 
            ResultSet set = sampleQueryStatement.executeQuery();
            sampleQueryStatement.close();
            conn.close();
            return set;
    	}catch (SQLException e) {
    		logger.warning("Could not execute SQL query! [" + query + "]");
    		return null;
		}
    }
}
