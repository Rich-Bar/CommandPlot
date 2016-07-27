package richbar.com.github.commandplot.scoreboard.objects;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.caching.CacheObject;

@SuppressWarnings("serial")
public class TeamObject extends CacheObject<TeamObject>{

	PlotId plotId;
	String displayName, name, color;
	
	boolean allowFriendlyFire, SeeFriendlyInvisibles,
	nameTagsOwnTeam, nameTagsOtherTeam,
	collisionOwnTeam, collissionOtherTeams,
	deathMessageOwnTeam, deathMessageOtherTeams;
	
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
		// TODO Auto-generated method stub
		return null;
	}

}
