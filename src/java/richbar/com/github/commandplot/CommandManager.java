package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import org.bukkit.craftbukkit.v1_9_R1.command.VanillaCommandWrapper;
import net.minecraft.server.v1_9_R1.CommandAbstract;
import net.minecraft.server.v1_9_R1.CommandBlockData;
import net.minecraft.server.v1_9_R1.CommandClear;
import net.minecraft.server.v1_9_R1.CommandClone;
import net.minecraft.server.v1_9_R1.CommandEffect;
import net.minecraft.server.v1_9_R1.CommandEnchant;
import net.minecraft.server.v1_9_R1.CommandEntityData;
import net.minecraft.server.v1_9_R1.CommandFill;
import net.minecraft.server.v1_9_R1.CommandGamemode;
import net.minecraft.server.v1_9_R1.CommandGive;
import net.minecraft.server.v1_9_R1.CommandKill;
import net.minecraft.server.v1_9_R1.CommandParticle;
import net.minecraft.server.v1_9_R1.CommandPlaySound;
import net.minecraft.server.v1_9_R1.CommandReplaceItem;
import net.minecraft.server.v1_9_R1.CommandSay;
import net.minecraft.server.v1_9_R1.CommandSetBlock;
import net.minecraft.server.v1_9_R1.CommandSummon;
import net.minecraft.server.v1_9_R1.CommandTell;
import net.minecraft.server.v1_9_R1.CommandTellRaw;
import net.minecraft.server.v1_9_R1.CommandTestFor;
import net.minecraft.server.v1_9_R1.CommandTitle;
import net.minecraft.server.v1_9_R1.CommandTp;
import net.minecraft.server.v1_9_R1.CommandTrigger;
import net.minecraft.server.v1_9_R1.CommandXp;

import richbar.com.github.commandplot.command.CommandReceive;
import richbar.com.github.commandplot.command.CommandTransmit;
import richbar.com.github.commandplot.command.ExecuteSender;

public class CommandManager implements CommandExecutor{
	public enum Commands{
		
		SAY		(new CommandSay(), elemType.REST),
		CLEAR	(new CommandClear(), elemType.PLAYER, elemType.REST),
		ENTITYDATA(new CommandEntityData(),elemType.ENTITY, elemType.REST),
		REPLACEITEM(new CommandReplaceItem(), elemType.ARG, elemType.ENTITYorCOORD, elemType.REST),
		REPLACEITEMCOORD(new CommandReplaceItem(), elemType.ARG, elemType.COORDS, elemType.REST),
		TRIGGER(new CommandTrigger(), elemType.REST),
		
		//SCOREBOARD(new CommandScoreboard(), elemType.REST), //TODO: Implement Scoremoard
		//EXECUTE(new CommandExecute(), elemType.ENTITY, elemType.DCOORDS, elemType.COMMAND),
		
		RECEIVE	(new CommandReceive(), elemType.REST),
		TRANSMIT(new CommandTransmit(), elemType.REST),
		
		KILL	(new CommandKill(), elemType.ENTITY),
		TESTFOR	(new CommandTestFor(), elemType.PLAYER, elemType.REST),
		TELL	(new CommandTell(), elemType.PLAYER, elemType.REST),
		TELLRAW	(new CommandTellRaw(), elemType.PLAYER, elemType.REST),
		EFFECT	(new CommandEffect(), elemType.ENTITY, elemType.REST),
		ENCHANT	(new CommandEnchant(), elemType.PLAYER, elemType.REST),
		GAMEMODE(new CommandGamemode(), elemType.ARG, elemType.PLAYER),
		TITLE	(new CommandTitle(), elemType.PLAYER, elemType.REST),
		GIVE	(new CommandGive(), elemType.PLAYER, elemType.ARG, elemType.REST),
		XP		(new CommandXp(), elemType.ARG, elemType.PLAYER),
		TP		(new CommandTp(), elemType.PLAYER, elemType.ENTITYorCOORD),
		TPCOORD	(new CommandTp(), elemType.PLAYER, elemType.COORDS),
		SETBLOCK(new CommandSetBlock(), elemType.COORDS, elemType.REST),
		BLOCKDATA(new CommandBlockData(),elemType.COORDS, elemType.REST),
		FILL	(new CommandFill(), elemType.COORDS, elemType.COORDS, elemType.REST),
		CLONE	(new CommandClone(), elemType.COORDS, elemType.COORDS, elemType.DCOORDS, elemType.REST),
		SUMMON	(new CommandSummon(), elemType.MOB, elemType.COORDS, elemType.REST),
		PARTICLE(new CommandParticle(), elemType.ARG, elemType.COORDS, elemType.DCOORDS, elemType.REST),
		PLAYSOUND(new CommandPlaySound(),elemType.ARG, elemType.ARG, elemType.PLAYER, elemType.COORDS, elemType.MAX2, elemType.MAX2, elemType.MAX1);
		
		private CommandAbstract id;
		private elemType[] elements;
		private Commands(CommandAbstract typeID, elemType... elements) {
			this.elements = elements;
			id = typeID;
		}
		public elemType[] getElements(){
			return elements;
		}
		public CommandAbstract getInst(){
			return id;
		}
		/**
		 * Returns the actual argument index of the command based of the complex command elements.
		 * @param complexIndex
		 * @return
		 */
		public int getIndex(int complexIndex){
			int res = 0;
			for(int i = 0; i < complexIndex; i++){
				switch(elements[i]){
				case MOB:
				case PLAYER:
				case ENTITY:
				case ENTITYorCOORD:
				case ARG:
				case MAX1:
				case MAX2: 
					res++;
					break;
				case COORDS:
				case DCOORDS:
					res += 3;
				default:
				break;
				}
			}
			return res;
		}
	}
	public static enum elemType{
		MOB, ENTITY, COORDS, DCOORDS, ARG, MAX1, MAX2, REST, PLAYER, COMMAND, ENTITYorCOORD;
	}
	CPlugin main;
	
	public CommandManager(CPlugin cPlugin) {
		main = cPlugin;
	}

	
	
	@SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Location blockpos = new Location(main.getServer().getWorlds().get(0), 0, 0, 0, 0, 0);
        boolean isCart = false;
        
		if(sender instanceof BlockCommandSender) {
            // Commandblock executed command
			blockpos = ((BlockCommandSender) sender).getBlock().getLocation();
        }else if(sender instanceof CommandMinecart || sender instanceof ExecuteSender) {
            // Minecart Commandlock executed command
        	blockpos = ((CommandMinecart) sender).getLocation();
        	isCart = true;
        }else{
        	return onVanilla(sender, label, args);
        }
		
		
		if(!main.check.getAPI().isPlotWorld(blockpos.getWorld()))return onVanilla(sender, label, args);
		if(!(main.getWhitelist().contains(label.toLowerCase()) || !main.check.canRun(blockpos))) return false;
		
    	try{
    		Commands commandType = Commands.valueOf(label.toUpperCase());
    		elemType[] elements = commandType.getElements();
    		
    		if(commandType.ordinal() == Commands.REPLACEITEM.ordinal() && ! args[1].toLowerCase().contains("slot")) 
    			commandType = Commands.REPLACEITEMCOORD;
        	if(commandType.ordinal() == Commands.TP.ordinal() && args.length > 2)
        		commandType = Commands.TPCOORD; 
    		
    		Location prev = null;
    		List<Object> artifacts = new ArrayList<>();
    		List<String> invalidArgs = new ArrayList<>();
    		
    		int i = 0;
    		for(elemType element : elements){
    			if(element == elemType.REST || commandType.getIndex(i) >= args.length) break;
    			switch (element) {
    				case ARG:
    					if(commandType.ordinal() == Commands.GIVE.ordinal() && args[1].replace(":", "").contains(":")){
    						String tmp = args[1].substring(args[1].lastIndexOf(":"), args[1].length());
    						if(args.length == 4)
    							args = new String[]{args[0], args[1], args[2], tmp, args[3]};
    						else
    							args = new String[]{args[0], args[1], args[2], tmp};
    					}
    					break;
					case MOB:
						String[] blacklistedMobs = {"PrimedTnt", "Endermite", "Silverfish", "Ghast", "Enderman", "Blaze", "WitherBoss", "EnderDragon", "FallingSand", "ShulkerBullet"};
						String requested = args[commandType.getIndex(i)];
						artifacts.add(requested);
						for(String blackMob : blacklistedMobs)
							if(blackMob.equals(requested)) invalidArgs.add(commandType.getIndex(i)+ "");
						break;
						
						
					case PLAYER:
						Player player = getPlayer(args[commandType.getIndex(i)]);
						artifacts.add(player);
						if(player == null) invalidArgs.add(commandType.getIndex(i)+ "");
						if(!main.check.isSamePlot(blockpos, player.getLocation())) invalidArgs.add(commandType.getIndex(i)+ "");
						break;
						
						
					case ENTITYorCOORD:
					case ENTITY:
						Map<UUID, Entity> uuidSet = getUUIDset((Entity[]) blockpos.getWorld().getEntities().toArray());
 						Entity e  = uuidSet.get(args[commandType.getIndex(i)]);
 						artifacts.add(e);
						if(e == null) invalidArgs.add(commandType.getIndex(i)+ "");
						if(!main.check.isSamePlot(blockpos, e.getLocation())) invalidArgs.add(commandType.getIndex(i)+ "");
						break;
						
						
					case COORDS:
						int indeZ = commandType.getIndex(i+1) -1, 
						indeY = indeZ -1,
						indeX = indeY -1;
						
						isLocation nLoc = getLocation(blockpos, args[indeX], args[indeY], args[indeZ]);
						artifacts.add(nLoc);
						
						if(!main.check.isSamePlot(blockpos, nLoc)) 
							invalidArgs.add(indeX + "");
						
						if(commandType.ordinal() == Commands.CLONE.ordinal()){
							prev = i == 0? nLoc : prev.subtract(nLoc.getX(), nLoc.getY(), nLoc.getZ());
						}
						break;
						
						
					case DCOORDS:
						indeZ = commandType.getIndex(i+1) -1;
						indeY = indeZ -1;
						indeX = indeY -1;
						
						nLoc = getLocation(blockpos, args[indeX], args[indeY], args[indeZ]);
						artifacts.add(nLoc);
						
						if(commandType.ordinal() == Commands.CLONE.ordinal()){
							if(	!main.check.isSamePlot(blockpos, nLoc) ||
								!main.check.isSamePlot(nLoc, nLoc.add(prev.getX(), prev.getY(), prev.getZ())))
									invalidArgs.add(indeX + "");
	
						}else if(commandType.ordinal() == Commands.PARTICLE.ordinal()){
							if(nLoc.getX() > 16) args[indeX] = 16 + "";
							if(nLoc.getY() > 16) args[indeY] = 16 + "";
							if(nLoc.getZ() > 16) args[indeZ] = 16 + "";
						}
						break;
						
						
					case COMMAND:
						
						if(((isLocation) artifacts.get(1)).isRelative){
							if(isCart) sender = new ExecuteSender(((isLocation) artifacts.get(1)).clone());
						}
						String detect = args[commandType.getIndex(i)];
						if(detect.equalsIgnoreCase("detect")){
							String full = Arrays.toString(args);
							detect = full.substring(full.indexOf("/"), args.length);
						}
						System.out.println("Execute next: " + detect);
						//Commands newCommand = Commands.valueOf(detect);
						//return onCommand(sender, newCommand.id, label, args);
						break;
						
						
					case MAX1:
						double dm1 = Double.parseDouble(args[commandType.getIndex(i)]);
						if(dm1 > 1.00) dm1 = 1.00;
						else if(dm1 < 0.00) dm1 = 0.00;
						args[commandType.getIndex(i)] = dm1 +"";
						artifacts.add(dm1);
						break;
						
						
					case MAX2:
						double dm2 = Double.parseDouble(args[commandType.getIndex(i)]);
						if(dm2 > 1.00) dm2 = 1.00;
						else if(dm2 < 0.00) dm2 = 0.00;
						args[commandType.getIndex(i)] = dm2 +"";
						artifacts.add(dm2);
					default:break;
					
				}
    			i++;
    		}
    		if(invalidArgs.size() == 0) return onVanilla(sender, label, args);
    		sender.sendMessage("Command Execution failed! The following args are the reason:");
    		for(String f : invalidArgs) sender.sendMessage(f + ": " + args[Integer.parseInt(f)]);
    		return false;
    	}catch(IllegalArgumentException|NullPointerException exc){
    		Logger logger = main.getLogger();
    		logger.info("Command Execution failed! Exception:");
    		exc.printStackTrace();
    		return false;
    	}
    }
	
	public boolean onVanilla(CommandSender sender, String label, String... args)
	{
    	try{
    		Commands commandType = Commands.valueOf(label.toUpperCase());
    		VanillaCommandWrapper wrap = new VanillaCommandWrapper(commandType.id);
    		return wrap.execute(sender, label, args);
    	}catch(IllegalArgumentException|NullPointerException exc){
    		return false;
    	}
	}

	public Player getPlayer(String name){
		List<Player> matches = null;
		try {
		    matches = main.getServer().matchPlayer(name);
		} catch (Exception e) {}
		if(matches != null) return matches.get(0);
		return null;
	}
	
	public isLocation getLocation(Location blockpos, String x, String y, String z){
		double worldX = 0, worldY = 0, worldZ = 0;
		boolean isRelative = false;
		if(x.contains("~")){
			x = x.replace("~", "");
			worldX += blockpos.getX();
			isRelative = true;
		}
		if(y.contains("~")){
			y = y.replace("~", "");
			worldY += blockpos.getY();
			isRelative = true;
		}
		if(z.contains("~")){
			z = z.replace("~", "");
			worldZ += blockpos.getZ();
			isRelative = true;
		}
		if(!x.isEmpty())worldX += Double.parseDouble(x);
		if(!y.isEmpty())worldY += Double.parseDouble(y);
		if(!z.isEmpty())worldZ += Double.parseDouble(z);
		return new isLocation(blockpos.getWorld(), worldX, worldY, worldZ, isRelative);
	}
	
	private Map<UUID, Entity> getUUIDset(Entity... es){
		Map<UUID, Entity> res = new HashMap<>();
		for(Entity e : es){
			res.put(e.getUniqueId(), e);
		}
		return res;
	}
	
	private class isLocation extends Location{

		public boolean isRelative;
		
		public isLocation(World world, double x, double y, double z, boolean isRelative) {
			super(world, x, y, z);
			this.isRelative = isRelative;
		}
		
		public isLocation(Location loc, boolean isRelative) {
			super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
			this.isRelative = isRelative;
		}
		
		
	}
	
	
}
