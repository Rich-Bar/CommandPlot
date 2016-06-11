package richbar.com.github.commandplot.sql;

import richbar.com.github.commandplot.sql.caching.SQLWrapper;

public class PlayerSQLWrapper extends SQLWrapper{

	@Override
	public String getTableName() {
		return "`%SCHEMA%`.`commandmode`";
	}

	@Override
	public String getTypeName() {
		return "UUID";
	}

	@Override
	public int getVarCharLimit() {
		return 38;
	}

	@Override 
	/*
		TODO: add Primary functionality!
	*/  
	public boolean hasPrimary() {
		return false;
	}
}
