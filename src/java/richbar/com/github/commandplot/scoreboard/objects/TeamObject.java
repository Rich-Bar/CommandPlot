package richbar.com.github.commandplot.scoreboard.objects;

import java.util.Arrays;

import com.intellectualcrafters.plot.object.PlotId;

import richbar.com.github.commandplot.scoreboard.caching.TeamWrapper;

public class TeamObject{

	public PlotId plotId;
	public String displayName;
	public String name;
	public String color;
	
	public boolean allowFriendlyFire, SeeFriendlyInvisibles,
	nameTagsOwnTeam, nameTagsOtherTeam,
	collisionOwnTeam, collissionOtherTeams,
	deathMessageOwnTeam, deathMessageOtherTeams;

	
	public TeamObject(PlotId pId, String name, String displayName, String color, int bs) {
		this(pId, name, displayName, color, TeamWrapper.getSettingsBoolean(bs));
	}

	public TeamObject(PlotId pId, String name, String displayName, String color, boolean...bs) {
		plotId = pId;
		this.name = name;
		this.displayName = displayName;
		this.color = color;
		boolean[] booleans = Arrays.copyOfRange(bs, 0, 8);
		this.allowFriendlyFire = booleans[0];
		this.collisionOwnTeam = booleans[1];
		this.collissionOtherTeams = booleans[2];
		this.deathMessageOtherTeams = booleans[3];
		this.deathMessageOwnTeam = booleans[4];
		this.nameTagsOtherTeam = booleans[5];
		this.nameTagsOwnTeam = booleans[6];
		this.SeeFriendlyInvisibles = booleans[7];
	}
}
