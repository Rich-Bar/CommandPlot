package richbar.com.github.commandplot.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

import richbar.com.github.commandplot.CPlugin;
import richbar.com.github.commandplot.CommandRouter;
import richbar.com.github.commandplot.backends.CommandBlockMode;
import richbar.com.github.commandplot.caching.objects.UUIDObject;

public class CommandAccessor implements Listener {

	private final CommandBlockMode cbMode;
	private final CommandRouter.PSChecker api;
	private final CPlugin main;
	
	public CommandAccessor(CPlugin main, CommandBlockMode cbMode) {
		this.cbMode = cbMode;
		this.main = main;
		api = new CommandRouter.PSChecker();
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
			p.sendMessage(main.messages.getColoredString("cbm-command-fail"));
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	private void onInteract(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null)
		    if(!api.isSamePlot(e.getPlayer().getLocation(), e.getClickedBlock().getLocation())) e.setCancelled(true);
	}

    @EventHandler
    public  void onBreakBlock(BlockBreakEvent event){
        if(!api.isSamePlot(event.getPlayer().getLocation(), event.getBlock().getLocation())) event.setCancelled(true);
    }

	@EventHandler
    public  void onPlaceBlock(BlockPlaceEvent event){
        if(!api.isSamePlot(event.getPlayer().getLocation(), event.getBlock().getLocation())) event.setCancelled(true);
    }
	
	@EventHandler
	public void onWorldSwitch(PlayerChangedWorldEvent e){
		Player p = e.getPlayer();
        if(cbMode.contains(new UUIDObject(p.getUniqueId()))) {
            p.sendMessage(main.messages.getColoredString("cbm-auto-disable"));
            cbMode.remove(new UUIDObject(p.getUniqueId()));
            if(!p.hasPermission("plots.commandblock.admin"))p.setOp(false);
        }
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		if(cbMode.contains(new UUIDObject(p.getUniqueId()))) {
            p.sendMessage(main.messages.getColoredString("cbm-auto-disable"));
            cbMode.remove(new UUIDObject(p.getUniqueId()));
            if(!p.hasPermission("plots.commandblock.admin"))p.setOp(false);
        }
	}

    @EventHandler
    public  void onTeleport(PlayerTeleportEvent event){
        Player p = event.getPlayer();
        if(api.isSamePlot(event.getFrom(), event.getTo())) return;
        if(cbMode.contains(new UUIDObject(p.getUniqueId()))) {
            p.sendMessage(main.messages.getColoredString("cbm-auto-disable"));
            cbMode.remove(new UUIDObject(p.getUniqueId()));
            if(!p.hasPermission("plots.commandblock.admin"))p.setOp(false);
        }
    }
}
