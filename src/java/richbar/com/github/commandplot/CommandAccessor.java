package richbar.com.github.commandplot;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftCommandBlock;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftMinecartCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.intellectualcrafters.plot.api.PlotAPI;

import net.minecraft.server.v1_9_R1.BlockCommand;
import net.minecraft.server.v1_9_R1.TileEntityCommand;

public class CommandAccessor implements Listener {

	PlotAPI api;
	
	public CommandAccessor(PlotAPI api){
		this.api = api;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getClickedBlock().getType() == Material.COMMAND){
			System.out.println("Rightclicked a 'CraftCommandBlock'");
		}else
			System.out.println(e.getClickedBlock().getClass().getCanonicalName());
	}
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e){
		if(e.getRightClicked().getType() == EntityType.MINECART_COMMAND){ //runs twice o.O
			System.out.println("Rightclicked a 'CraftMinecartCommand'");  
			e.getPlayer().setOp(true);
			System.out.println(((CraftMinecartCommand) e.getRightClicked()).getPassenger().getClass().getName());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(e.getPlayer().hasPermission("plots.admin.op")) return;
		if(e.getPlayer().isOp()) e.getPlayer().setOp(false);
	}
	@EventHandler
	public void onWorldSwitch(PlayerChangedWorldEvent e){
		if(e.getPlayer().hasPermission("plots.admin.op")) return;
		if(e.getPlayer().isOp()) e.getPlayer().setOp(false);
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		if(e.getPlayer().hasPermission("plots.admin.op")) return;
		if(e.getPlayer().isOp()) e.getPlayer().setOp(false);
	}
	
}
