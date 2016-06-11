package richbar.com.github.commandplot;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class CommandAccessor implements Listener {

	private CommandBlockMode cbMode;
	private CPlugin main;
	
	public CommandAccessor(CPlugin main, CommandBlockMode cbMode) {
		this.cbMode = cbMode;
		this.main = main;
	}
	
	List<String> whitelist = Arrays.asList(new String[]{"commandblockmode", "cbm", "commandblock", "cb"});
	
	@EventHandler
	public void preCommand(PlayerCommandPreprocessEvent e) throws SQLException{
		Player p = e.getPlayer();
		String[] args = e.getMessage().replace("/", "").split(" ");
		String cmd = args[0];
		if(whitelist.contains(cmd.toLowerCase())) return;
		if((cmd.equalsIgnoreCase("gm") || cmd.equalsIgnoreCase("gamemode")) && args.length < 2) return;
		if(cbMode.isActive(p.getUniqueId())){
			p.sendMessage(main.messages.getString("cbm-command-fail"));
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onWorldSwitch(PlayerChangedWorldEvent e) throws SQLException{
		Player p = e.getPlayer();
		p.sendMessage(main.messages.getString("cbm-auto-disable"));
		cbMode.removePlayer(p.getUniqueId());
		p.setOp(false);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		p.sendMessage(main.messages.getString("cbm-auto-disable"));
		cbMode.removePlayer(p.getUniqueId());
		p.setOp(false);
	}	
}
