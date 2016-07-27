package richbar.com.github.commandplot.scoreboard.objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.util.ArmorCalc;

public class TrackCriteria{

	private enum trackCriterias{
		health, xp, level, food, air, armor;
	}

	public static int getValue(Player p, String criteriaName) {
		switch(trackCriterias.valueOf(criteriaName.toLowerCase())){
		case air: return p.getRemainingAir();
		case armor: return (int) (ArmorCalc.getArmor(p) * 23);
		case food: return p.getFoodLevel();
		case health: return (int) p.getHealth();
		case level: return p.getLevel();
		case xp: return (int) p.getExp();
		}
		return -1;
	}

	public static int getValue(Entity e, String criteriaName) {
		if(e instanceof LivingEntity){
			LivingEntity livE = (LivingEntity) e;
			switch(trackCriterias.valueOf(criteriaName.toLowerCase())){
			case air: return livE.getRemainingAir();
			case health: return (int) livE.getHealth();
			default: break;
			}
		}
		return -1;
	}
}
