package richbar.com.github.commandplot.command;

import richbar.com.github.commandplot.CPlugin;

public class CommandPlotCommand extends BranchingCommand{


	public CommandPlotCommand(CPlugin main) {
		subExecutors.put("alwaysactive", main.activePlots);
	}
}
