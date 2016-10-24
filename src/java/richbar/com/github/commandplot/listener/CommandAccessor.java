package richbar.com.github.commandplot.listener;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import richbar.com.github.commandplot.CPlugin;
import richbar.com.github.commandplot.backends.CommandBlockMode;
import richbar.com.github.commandplot.caching.objects.UUIDObject;

public class CommandAccessor implements Listener {

	private final CommandBlockMode cbMode;
	private final CPlugin main;
	
	public CommandAccessor(CPlugin main, CommandBlockMode cbMode) {
		this.cbMode = cbMode;
		this.main = main;
	}

	private final List<String> whitelist = Arrays.asList("commandblockmode", "cbm", "commandblock", "cb");
	
	@EventHandler
	public void preCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String[] args = e.getMessage().replace("/", "").split(" ");
		String cmd = args[0];
		if(whitelist.contains(cmd.toLowerCase())) return;
		if((cmd.equalsIgnoreCase("gm") || cmd.equalsIgnoreCase("gamemode")) && args.length < 2) return;
		if(cbMode.contains(new UUIDObject(p.getUniqueId()))){
			p.sendMessage(main.messages.getString("cbm-command-fail"));
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	private void onInteract(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null)
		if(e.getClickedBlock().getType() == Material.COMMAND)
			if(!main.check.isInPlot(e.getPlayer()))e.setCancelled(true);
	}
	
	@EventHandler
	public void onWorldSwitch(PlayerChangedWorldEvent e){
		Player p = e.getPlayer();
		p.sendMessage(main.messages.getString("cbm-auto-disable"));
		cbMode.remove(new UUIDObject(p.getUniqueId()));
		p.setOp(false);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		p.sendMessage(main.messages.getString("cbm-auto-disable"));
		cbMode.remove(new UUIDObject(p.getUniqueId()));
		p.setOp(false);
	}	
}
