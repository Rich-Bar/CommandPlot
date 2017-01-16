package richbar.com.github.commandplot.caching.sql;

public class PlotIdSQLWrapper extends SQLWrapper{

	@Override
	public String getTableName() {
		return "`%SCHEMA%`.`activePlot`";
	}

	@Override
	public String getTypeName() {
		return "PLOT";
	}

	@Override
	public int getVarCharLimit() {
		return 10;
	}

	@Override
	public boolean hasPrimary() {
		return true;
	}

}
