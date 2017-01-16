package richbar.com.github.commandplot.util;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.RegionWrapper;
import net.minecraft.server.v1_10_R1.*;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_10_R1.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import richbar.com.github.commandplot.command.ExecuteSender;
import richbar.com.github.commandplot.command.TestForSender;

import java.util.*;

public class Util {


    public static Location getSenderLoc(CommandSender sender){
        Location blockpos = null;
        if(sender instanceof BlockCommandSender) {
            // Commandblock executed command
            blockpos = ((BlockCommandSender) sender).getBlock().getLocation();
        }else if(sender instanceof CommandMinecart) {
            // Minecart Commandlock executed command
            blockpos = ((CommandMinecart) sender).getLocation();
        }else if(sender instanceof ExecuteSender) {
            // Execute
            blockpos = ((ExecuteSender) sender).getLocation();
        }else if(sender instanceof Player) {
            blockpos = ((Player) sender).getLocation();
        }
        return blockpos;
    }
	
	public static ICommandListener getListener(CommandSender sender) {
		if (sender instanceof ExecuteSender) {
			return getListener(((ExecuteSender) sender).getSender());
		} else if (sender instanceof TestForSender) {
			return getListener(((TestForSender) sender).getSender());
		} else if (sender instanceof Player) {
			return ((CraftPlayer) sender).getHandle();
		} else if (sender instanceof BlockCommandSender) {
			return ((CraftBlockCommandSender) sender).getTileEntity();
		} else if (sender instanceof CommandMinecart) {
			return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlock();
		} else if (sender instanceof RemoteConsoleCommandSender) {
			return ((DedicatedServer) MinecraftServer.getServer()).remoteControlCommandListener;
		} else if (sender instanceof ConsoleCommandSender) {
			return ((CraftServer) sender.getServer()).getServer();
		} else if (sender instanceof ProxiedCommandSender) {
			return ((ProxiedNativeCommandSender) sender).getHandle();
		} else {
			throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
		}
	}

    public static Entity getEntity(World w, UUID arg){
        for (Entity entity : w.getEntities()) {
            if (entity.getUniqueId().equals(arg))
                return entity;
        }
        return null;
    }


    public static boolean checkNBT(Entity e, String arg){
        try {
            NBTTagCompound compound = MojangsonParser.parse(arg);
            NBTTagCompound content = ((CraftEntity)e).getHandle().e(new NBTTagCompound());
            boolean res = checkNBT(content, compound);
            //Bukkit.getLogger().info("Checking NBT resulted " + res);
            return res;
        }catch(MojangsonParseException exc){
            return false;
        }
    }

    private static boolean checkNBT(NBTTagCompound content, NBTTagCompound compound){
        boolean res = true;
        for(String name : compound.c()){
            if(content.hasKey(name))
                if(content.get(name) instanceof  NBTTagCompound)
                    res = res && checkNBT((NBTTagCompound)content.get(name), (NBTTagCompound)compound.get(name));
                else
                    res = res && content.get(name).equals(compound.get(name));
            else return false;
        }
        return res;
    }

    public static Player getPlayer(String name){
        Player p = Bukkit.getServer().getPlayer(name);
        if(p == null) p = Bukkit.getServer().getPlayer(UUID.fromString(name));
        return p;
    }

    public static Map<UUID, Entity> getUUIDset(Set<Entity> es){
        Map<UUID, Entity> res = new HashMap<>();
        for(Entity e : es) res.put(e.getUniqueId(), e);
        return res;
    }

    public static Set<Chunk> getAllChunks(Plot p){
        World w = Bukkit.getWorld(p.getArea().worldname);
        HashSet<Chunk> chunks = new HashSet<>();
        for (RegionWrapper region : p.getRegions()) {
            for (int x = region.minX >> 4; x <= region.maxX >> 4; x++) {
                for (int z = region.minZ >> 4; z <= region.maxZ >> 4; z++) {
                    chunks.add(w.getChunkAt(x, z));
                }
            }
        }
        return chunks;
    }

    public static Set<Entity> getEntitiesInPlot(Plot plot){
        Set<Entity> entities = new HashSet<>();

        for(Chunk c : getAllChunks(plot)){
            entities.addAll(Arrays.asList(c.getEntities()));
        }
        return entities;
    }

    public static boolean insideRegion(Location loc, Location c1, Location c2){
        return ((loc.getX() > c1.getX() && loc.getX() < c1.getX()) &&
               (loc.getY() > c1.getY() && loc.getY() < c1.getY()) &&
               (loc.getX() > c1.getX() && loc.getX() < c1.getX()))||
               (loc.getX() < c1.getX() && loc.getX() > c1.getX()) &&
               (loc.getY() < c1.getY() && loc.getY() > c1.getY()) &&
               (loc.getX() < c1.getX() && loc.getX() > c1.getX());
    }

    public static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> sortByValue(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>(
                (Comparator<Map.Entry<K, V>>) (e1, e2) -> {
                    int res = e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1;
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
