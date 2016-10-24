package richbar.com.github.commandplot.caching.sql;

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
	public boolean hasPrimary() {
		return false;
	}
}
