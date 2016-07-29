package richbar.com.github.commandplot.scoreboard.objects;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
public class TeamObject extends CacheObject<TeamObject>{

	public PlotId plotId;
	public String displayName;
	public String name;
	public String color;
	
	public boolean allowFriendlyFire, SeeFriendlyInvisibles,
	nameTagsOwnTeam, nameTagsOtherTeam,
	collisionOwnTeam, collissionOtherTeams,
	deathMessageOwnTeam, deathMessageOtherTeams;
	
	public TeamObject() {
		// TODO Auto-generated constructor stub
	}
	
	public TeamObject(PlotId pId, String name, String displayName, int color, boolean...bs) {
		// TODO Auto-generated constructor stub
	}	

	public TeamObject(PlotId pId, String name, String displayName, String color, boolean...bs) {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public TeamObject getObject() {
		return this;
	}
	
	@Override
	public String toString() {
		return null;
	}

	@Override
	public TeamObject fromString(String serialized) {
		return null;
	}

}
