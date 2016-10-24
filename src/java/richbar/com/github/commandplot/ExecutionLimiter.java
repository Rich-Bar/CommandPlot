package richbar.com.github.commandplot;

import java.util.HashMap;
import java.util.Map;

import com.intellectualcrafters.plot.object.PlotId;

public class ExecutionLimiter implements Runnable{

	private final Map<PlotId, Integer> counter = new HashMap<>();
	private int limit = 200;
	
	public ExecutionLimiter(CPlugin main) {
		limit = main.getConfig().getInt("limits.maxExecutions");
	}
	
	@Override
	public void run() {
		counter.clear();
	}
	
	public void add(PlotId id){
		if(counter.get(id) == null) counter.put(id, 1);
		else counter.replace(id, counter.get(id) + 1);
	}
	
	public boolean check(PlotId id){
		return counter.get(id) == null || counter.get(id) <= limit;
	}

}
