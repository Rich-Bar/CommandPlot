package richbar.com.github.commandplot.scoreboard.objects;

import com.intellectualcrafters.plot.object.PlotId;
import richbar.com.github.commandplot.scoreboard.caching.TeamWrapper;
import richbar.com.github.commandplot.util.TeamColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TeamObject{

	public PlotId plotId;
	public String displayName;
	public String name;
	public TeamColor color;
	
	public boolean allowFriendlyFire, SeeFriendlyInvisibles,
	nameTagsOwnTeam, nameTagsOtherTeam,
	collisionOwnTeam, collissionOtherTeams,
	deathMessageOwnTeam, deathMessageOtherTeams;

	public List<UUID> members = new ArrayList<>();
	
	public TeamObject(PlotId pId, String name, String displayName, TeamColor color, int bs) {
		this(pId, name, displayName, color, TeamWrapper.getSettingsBoolean(bs));
	}

	public TeamObject(PlotId pId, String name, String displayName, TeamColor color, boolean...bs) {
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
