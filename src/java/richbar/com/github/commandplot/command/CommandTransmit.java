package richbar.com.github.commandplot.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.metadata.FixedMetadataValue;

import net.minecraft.server.v1_9_R1.ICommandListener;
import richbar.com.github.commandplot.util.IsLocation;

public class CommandTransmit extends CustomCommand{
	
	@Override
	public String getCommand() {
		return "transmit";
	}

	@Override
	public String getUsage(ICommandListener arg0) {
		return "[only Commandblocks!] use 'transmit <x y z>' to trigger the commandblock at pos <x y z>. The Command block to receive needs to specify your blocks pos!";
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Location blockpos = null;
		
		if(sender instanceof BlockCommandSender) {
			blockpos = ((BlockCommandSender) sender).getBlock().getLocation();
        }else if(sender instanceof CommandMinecart) {
        	blockpos = ((CommandMinecart) sender).getLocation();
        }else if(sender instanceof ExecuteSender) {
            blockpos = ((ExecuteSender) sender).getLocation();
        }else if(sender instanceof Player) {
            blockpos = ((Player) sender).getLocation();
        }else return false;
		
		IsLocation nLoc = new IsLocation(blockpos, args[0], args[1], args[2]);
		
		if(nLoc.getBlock().getType() != Material.COMMAND) return false;
		nLoc.getBlock().setMetadata("powered", new FixedMetadataValue(null, true));;
		
		
		return true;
	}
}
