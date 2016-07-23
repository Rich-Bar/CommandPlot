package richbar.com.github.commandplot.scoreboard.objects;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.scoreboard.AchievementLookup;
import richbar.com.github.commandplot.scoreboard.StatisticLookup;

@SuppressWarnings("serial")
public class StatisticCriteria extends CriteriaObject{

	public StatisticCriteria(String criteriaName) {
		super(criteriaName);
	}

	@Override
	public int getValue(Player p) {
		if(getCriteriaName().startsWith("stat.")){
			boolean singePoint = getCriteriaName().indexOf(".") == getCriteriaName().lastIndexOf(".");
			String stat = (singePoint ? getCriteriaName() : getCriteriaName().substring(0, getCriteriaName().lastIndexOf("."))).replace(".", "_");
			String value = (singePoint ? "" : getCriteriaName().substring(getCriteriaName().lastIndexOf(".") + 1, getCriteriaName().length())).replace(".", "_");
			if(!value.isEmpty()){
				@SuppressWarnings("deprecation")
				EntityType eType = EntityType.fromName(value);
				if(eType != null) 
					return p.getStatistic((Statistic) StatisticLookup.valueOf(stat).getStatistic(), eType);
				Material mType = Material.getMaterial(value);
				if(mType != null) 
					return p.getStatistic((Statistic) StatisticLookup.valueOf(stat).getStatistic(), mType);
			}
			return p.getStatistic((Statistic) StatisticLookup.valueOf(stat).getStatistic());
		
		
		}else if(getCriteriaName().startsWith("achievement.")){
			String stat = getCriteriaName().replace(".", "_");
			return p.hasAchievement(AchievementLookup.valueOf(stat).getAchievement()) ? 1 : 0;
		}
		return -1;
	}

	@Override
	public int getValue(Entity e) {
		return -1;
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
