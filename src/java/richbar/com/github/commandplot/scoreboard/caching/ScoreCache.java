package richbar.com.github.commandplot.scoreboard.caching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.scoreboard.objects.ObjectiveObject;

public class ScoreCache {
	
	private Map<LivingEntity, List<Map<ObjectiveObject, Integer>>> scores = new HashMap<>();
	
	public void getScore(LivingEntity e){
		if(e instanceof Player){
			Player p = (Player) e;
		}
	}
	
}
