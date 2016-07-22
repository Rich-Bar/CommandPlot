package richbar.com.github.commandplot.scoreboard;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
public class CriteriaObject extends CacheObject<CriteriaObject>{

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

	public int getValue(Player p){
		if(criteriaName.startsWith("stat.")){
			boolean singePoint = criteriaName.indexOf(".") == criteriaName.lastIndexOf(".");
			String stat = (singePoint ? criteriaName : criteriaName.substring(0, criteriaName.lastIndexOf("."))).replace(".", "_");
			String value = (singePoint ? "" : criteriaName.substring(criteriaName.lastIndexOf(".") + 1, criteriaName.length())).replace(".", "_");
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
		
		
		}else if(criteriaName.startsWith("achievement.")){
			String stat = criteriaName.replace(".", "_");
			return p.hasAchievement(AchievementLookup.valueOf(stat).getAchievement()) ? 1 : 0;
		}
		return 0;
		
	}
	
	public int getValue(Entity e){
		return 0;
		
	}
}
