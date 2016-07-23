package richbar.com.github.commandplot.scoreboard.objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.util.ArmorCalc;

@SuppressWarnings("serial")
public class TrackCriteria extends CriteriaObject{

	private enum criterias{
		health, xp, level, food, air, armor;
	}
	
	public TrackCriteria(String criteriaName) {
		super(criteriaName);
	}

	@Override
	public int getValue(Player p) {
		switch(criterias.valueOf(getCriteriaName().toLowerCase())){
		case air: return p.getRemainingAir();
		case armor: return (int) (ArmorCalc.getArmor(p) * 23);
		case food: return p.getFoodLevel();
		case health: return (int) p.getHealth();
		case level: return p.getLevel();
		case xp: return (int) p.getExp();
		}
		return -1;
	}

	@Override
	public int getValue(Entity e) {
		if(e instanceof LivingEntity){
			LivingEntity livE = (LivingEntity) e;
			switch(criterias.valueOf(getCriteriaName().toLowerCase())){
			case air: return livE.getRemainingAir();
			case health: return (int) livE.getHealth();
			default: break;
			}
		}
		return -1;
	}

	@Override
	public void increment(int by) {}

	@Override
	public void decrement(int by) {}

	@Override
	public void set(int val) {}

}
