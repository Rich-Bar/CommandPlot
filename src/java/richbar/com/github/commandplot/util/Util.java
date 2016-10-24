package richbar.com.github.commandplot.util;

import net.minecraft.server.v1_10_R1.MojangsonParseException;
import net.minecraft.server.v1_10_R1.MojangsonParser;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import richbar.com.github.commandplot.command.ExecuteSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public static Map<UUID, Object> getUUIDset(Object... es){
        Map<UUID, Object> res = new HashMap<>();
        for(Object e : es){
            if(e instanceof Player)res.put(((Player) e).getUniqueId(), e);
            else if(e instanceof Entity)res.put(((Entity) e).getUniqueId(), e);
        }
        return res;
    }

}
