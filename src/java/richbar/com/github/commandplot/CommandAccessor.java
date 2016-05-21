package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftCommandBlock;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftMinecartCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.intellectualcrafters.plot.api.PlotAPI;

import net.minecraft.server.v1_9_R1.BlockCommand;
import net.minecraft.server.v1_9_R1.CommandBlockListenerAbstract;
import net.minecraft.server.v1_9_R1.EntityMinecartCommandBlock;
import net.minecraft.server.v1_9_R1.TileEntityCommand;

public class CommandAccessor implements Listener {

	List<UUID> tempOpped = new ArrayList<>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getClickedBlock().getType() == Material.COMMAND && e.getAction() == Action.RIGHT_CLICK_BLOCK){
			System.out.println("Rightclicked a 'CraftCommandBlock'");
			Player p = e.getPlayer();
			
			if(tempOpped.contains(p.getUniqueId())){
				p.setOp(false);
				tempOpped.remove(p.getUniqueId());
			}
			else{
				p.setOp(true);
				tempOpped.add(p.getUniqueId());
				
			}
			if(p.hasPermission("plots.admin.op")) p.setOp(true);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e){
		if(e.getRightClicked().getType() == EntityType.MINECART_COMMAND){
			System.out.println("Rightclicked a 'CraftMinecartCommand' [" + e.getRightClicked() + "]");  
			Player p = e.getPlayer();
			
			if(tempOpped.contains(p.getUniqueId())){
				p.setOp(false);
				tempOpped.remove(p.getUniqueId());
			}
			else{
				p.setOp(true);
				tempOpped.add(p.getUniqueId());
			}
			if(p.hasPermission("plots.admin.op")) p.setOp(true);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(tempOpped.contains(p.getUniqueId())){
			p.setOp(false);
			tempOpped.remove(p.getUniqueId());
		}
		if(p.hasPermission("plots.admin.op")) p.setOp(true);
	}
	@EventHandler
	public void onWorldSwitch(PlayerChangedWorldEvent e){
		Player p = e.getPlayer();
		if(tempOpped.contains(p.getUniqueId())){
			p.setOp(false);
			tempOpped.remove(p.getUniqueId());
		}
		if(p.hasPermission("plots.admin.op")) p.setOp(true);
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		if(tempOpped.contains(p.getUniqueId())){
			p.setOp(false);
			tempOpped.remove(p.getUniqueId());
		}
		if(p.hasPermission("plots.admin.op")) p.setOp(true);
	}
	
}
