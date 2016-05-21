package richbar.com.github.commandplot;

import java.sql.SQLException;
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
import richbar.com.github.commandplot.util.SQLManager;
import richbar.com.github.commandplot.util.SQLWrapper;

public class CommandAccessor implements Listener {

	private CommandBlockMode cbMode;
	
	public CommandAccessor(CommandBlockMode cbMode) {
		this.cbMode = cbMode;
	}
	
	@EventHandler
	public void onWorldSwitch(PlayerChangedWorldEvent e) throws SQLException{
		Player p = e.getPlayer();
		cbMode.removePlayer(p.getUniqueId());
		p.setOp(false);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		cbMode.removePlayer(p.getUniqueId());
		p.setOp(false);
	}	
}
