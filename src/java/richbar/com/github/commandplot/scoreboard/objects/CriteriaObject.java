package richbar.com.github.commandplot.scoreboard.objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
public abstract class CriteriaObject extends CacheObject<CriteriaObject>{

	private String criteriaName;
	
	public CriteriaObject(String criteriaName) {
		this.criteriaName = criteriaName;
	}
	
	@Override
	public String toString() {
		return getCriteriaName();
	}

	@Override
	public CriteriaObject fromString(String serialized) {
		criteriaName = serialized;
		return this;
	}
	
	public String getCriteriaName(){
		return criteriaName;
	}

	public abstract int getValue(Player p);
	
	public abstract int getValue(Entity e);
	
	public abstract void increment(int by);
	
	public abstract void decrement(int by);
	
	public abstract void set(int val);
}
