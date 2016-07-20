package richbar.com.github.commandplot;

import java.util.HashMap;
import java.util.Map;

import com.intellectualcrafters.plot.object.PlotId;

public class ExecutionLimiter implements Runnable{

	Map<PlotId, Integer> counter = new HashMap<>();
	int limit = 200;
	
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
		if(counter.get(id) == null) return true;
		return counter.get(id) <= limit;
	}

}
