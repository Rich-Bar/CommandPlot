package richbar.com.github.commandplot.scoreboard.objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@SuppressWarnings("serial")
public class DummyCriteria extends CriteriaObject{

	public DummyCriteria(String criteriaName) {
		super(criteriaName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getValue(Player p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getValue(Entity e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void increment(int by) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrement(int by) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(int val) {
		// TODO Auto-generated method stub
		
	}

}
