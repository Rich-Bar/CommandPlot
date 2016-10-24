package richbar.com.github.commandplot.scoreboard.objects;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.scoreboard.AchievementLookup;
import richbar.com.github.commandplot.scoreboard.StatisticLookup;

public class StatisticCriteria{

	public static int getValue(Player p, String criteriaName) {
		if(criteriaName.startsWith("stat.")){
			boolean singePoint = criteriaName.indexOf(".") == criteriaName.lastIndexOf(".");
			String stat = (singePoint ? criteriaName : criteriaName.substring(0, criteriaName.lastIndexOf("."))).replace(".", "_");
			String value = (singePoint ? "" : criteriaName.substring(criteriaName.lastIndexOf(".") + 1, criteriaName.length())).replace(".", "_");
			if(!value.isEmpty()){
				@SuppressWarnings("deprecation")
				EntityType eType = EntityType.fromName(value);
				if(eType != null) 
					return p.getStatistic(StatisticLookup.valueOf(stat).getStatistic(), eType);
				Material mType = Material.getMaterial(value);
				if(mType != null) 
					return p.getStatistic(StatisticLookup.valueOf(stat).getStatistic(), mType);
			}
			return p.getStatistic(StatisticLookup.valueOf(stat).getStatistic());
		
		
		}else if(criteriaName.startsWith("achievement.")){
			String stat = criteriaName.replace(".", "_");
			return p.hasAchievement(AchievementLookup.valueOf(stat).getAchievement()) ? 1 : 0;
		}
		return -1;
	}
}
