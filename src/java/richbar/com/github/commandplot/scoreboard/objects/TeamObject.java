package richbar.com.github.commandplot.scoreboard.objects;

import com.intellectualcrafters.plot.object.PlotId;
import richbar.com.github.commandplot.scoreboard.caching.TeamWrapper;
import richbar.com.github.commandplot.util.TeamColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TeamObject{

	private final PlotId plotId;
	public final String displayName;
	public final String name;
	public final TeamColor color;
	
	public final boolean allowFriendlyFire;
	public final boolean SeeFriendlyInvisibles;
	public boolean nameTagsOwnTeam;
	public boolean nameTagsOtherTeam;
	public boolean collisionOwnTeam;
	public boolean collisionOtherTeams;
	public boolean deathMessageOwnTeam;
	public boolean deathMessageOtherTeams;

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
		this.collisionOtherTeams = booleans[2];
		this.deathMessageOtherTeams = booleans[3];
		this.deathMessageOwnTeam = booleans[4];
		this.nameTagsOtherTeam = booleans[5];
		this.nameTagsOwnTeam = booleans[6];
		this.SeeFriendlyInvisibles = booleans[7];
	}
}
