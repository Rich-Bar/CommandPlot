package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.command.VanillaCommandWrapper;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import net.minecraft.server.v1_9_R1.CommandAbstract;
import net.minecraft.server.v1_9_R1.CommandBlockData;
import net.minecraft.server.v1_9_R1.CommandClone;
import net.minecraft.server.v1_9_R1.CommandEffect;
import net.minecraft.server.v1_9_R1.CommandEnchant;
import net.minecraft.server.v1_9_R1.CommandFill;
import net.minecraft.server.v1_9_R1.CommandGamemode;
import net.minecraft.server.v1_9_R1.CommandGive;
import net.minecraft.server.v1_9_R1.CommandKill;
import net.minecraft.server.v1_9_R1.CommandParticle;
import net.minecraft.server.v1_9_R1.CommandPlaySound;
import net.minecraft.server.v1_9_R1.CommandSay;
import net.minecraft.server.v1_9_R1.CommandSetBlock;
import net.minecraft.server.v1_9_R1.CommandSummon;
import net.minecraft.server.v1_9_R1.CommandTell;
import net.minecraft.server.v1_9_R1.CommandTellRaw;
import net.minecraft.server.v1_9_R1.CommandTestFor;
import net.minecraft.server.v1_9_R1.CommandTp;
import net.minecraft.server.v1_9_R1.CommandXp;
import richbar.com.github.commandplot.command.ReceiveCommand;
import richbar.com.github.commandplot.command.TransmitCommand;

public class CommandManager implements CommandExecutor{
	public enum Commands{
		SAY		(new CommandSay(), elemType.REST),
		RECEIVE	(new ReceiveCommand(), elemType.REST),
		TRANSMIT(new TransmitCommand(), elemType.REST),
		KILL	(new CommandKill(), elemType.PLAYER),
		TESTFOR	(new CommandTestFor(), elemType.PLAYER, elemType.REST),
		TELL	(new CommandTell(), elemType.PLAYER, elemType.REST),
		TELLRAW	(new CommandTellRaw(), elemType.PLAYER, elemType.REST),
		EFFECT	(new CommandEffect(), elemType.PLAYER, elemType.REST),
		ENCHANT	(new CommandEnchant(), elemType.PLAYER, elemType.REST),
		GAMEMODE(new CommandGamemode(), elemType.PLAYER, elemType.REST),
		//TITLE	(3, elemType.PLAYER, elemType.REST, Command),  --No Implementation found!
		GIVE	(new CommandGive(), elemType.PLAYER, elemType.REST),
		XP		(new CommandXp(), elemType.PLAYER, elemType.REST),
		TPPOS	(new CommandTp(), elemType.PLAYER, elemType.COORDS),
		TP		(new CommandTp(), elemType.PLAYER, elemType.PLAYER),
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
		MOB, PLAYER, COORDS, DCOORDS, ARG, MAX1, MAX2, REST;
	}
	CPlugin main;
	
	public CommandManager(CPlugin cPlugin) {
		main = cPlugin;
	}

	
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Location blockpos = new Location(main.getServer().getWorlds().get(0), 0, 0, 0, 0, 0);
        
		if(sender instanceof BlockCommandSender) {
            // Commandblock executed command
			blockpos = ((BlockCommandSender) sender).getBlock().getLocation();
        }else if(sender instanceof CommandMinecart) {
            // Minecart Commandlock executed command
        	blockpos = ((CommandMinecart) sender).getLocation();
        }else{
        	return onVanilla(sender, label, args);
        }
		
		if(!main.api.isPlotWorld(blockpos.getWorld())) return onVanilla(sender, label, args);
		if(	!main.getWhitelist().contains(label.toLowerCase()) ||
			!main.canRun(blockpos)) return false;
		
    	try{
    		Commands commandType = Commands.valueOf(label.toUpperCase());
    		if(commandType.ordinal() == Commands.TP.ordinal() && args.length > 3) commandType = Commands.TPPOS;
    		elemType[] elements = commandType.getElements();
    		int i = 0;
    		Location prev = null;
    		List<String> invalidArgs = new ArrayList<>();
    		for(elemType element : elements){
    			if(element == elemType.REST || commandType.getIndex(i) >= args.length) break;
    			switch (element) {
					case MOB:
						String[] blacklistedMobs = {"PrimedTnt", "Endermite", "Silverfish", "Ghast", "Enderman", "Blaze", "WitherBoss", "EnderDragon", "FallingSand", "ShulkerBullet"};
						String requested = args[commandType.getIndex(i)];
						for(String blackMob : blacklistedMobs)
							if(blackMob.equals(requested)) invalidArgs.add(commandType.getIndex(i)+ "");
						break;
						
						
					case PLAYER:
						Player player = getPlayer(args[commandType.getIndex(i)]);
						if(player == null) return false;
						if(!main.isSamePlot(blockpos, player.getLocation())) invalidArgs.add(commandType.getIndex(i)+ "");
						break;
						
						
					case COORDS:
						int indeZ = commandType.getIndex(i+1) -1, 
						indeY = indeZ -1,
						indeX = indeY -1;
						
						Location nLoc = getLocation(blockpos, args[indeX], args[indeY], args[indeZ]);
						System.out.println(nLoc.toString());
						
						if(!main.isSamePlot(blockpos, nLoc)) 
							invalidArgs.add(indeX + "");
						
						if(commandType.ordinal() == Commands.CLONE.ordinal()){
							prev = i == 0? nLoc : prev.subtract(nLoc.getX(), nLoc.getY(), nLoc.getZ());
							System.out.println(prev.toString());
						}
						break;
						
						
					case DCOORDS:
						indeZ = commandType.getIndex(i+1) -1;
						indeY = indeZ -1;
						indeX = indeY -1;
						
						nLoc = getLocation(blockpos, args[indeX], args[indeY], args[indeZ]);
						System.out.println(nLoc.toString());
						
						if(commandType.ordinal() == Commands.CLONE.ordinal()){
							if(	!main.isSamePlot(blockpos, nLoc) ||
								!main.isSamePlot(nLoc, nLoc.add(prev.getX(), prev.getY(), prev.getZ())))
									invalidArgs.add(indeX + "");
	
						}else if(commandType.ordinal() == Commands.PARTICLE.ordinal()){
							if(nLoc.getX() > 16) args[indeX] = 16 + "";
							if(nLoc.getY() > 16) args[indeY] = 16 + "";
							if(nLoc.getZ() > 16) args[indeZ] = 16 + "";
						}
						break;
						
						
					case MAX1:
						double dm1 = Double.parseDouble(args[commandType.getIndex(i)]);
						if(dm1 > 1.00) dm1 = 1.00;
						else if(dm1 < 0.00) dm1 = 0.00;
						args[commandType.getIndex(i)] = dm1 +"";
						break;
						
						
					case MAX2:
						double dm2 = Double.parseDouble(args[commandType.getIndex(i)]);
						if(dm2 > 1.00) dm2 = 1.00;
						else if(dm2 < 0.00) dm2 = 0.00;
						args[commandType.getIndex(i)] = dm2 +"";
					default:break;
					
				}
    			i++;
    		}
    		if(invalidArgs.size() == 0) return onVanilla(sender, label, args);
    		Logger logger = main.getLogger();
    		logger.info("Command Execution failed! The following args are the reason:");
    		for(String f : invalidArgs) logger.info(f + ": " + args[Integer.parseInt(f)]);
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
    		if(commandType == Commands.TP && args.length > 3) commandType = Commands.TPPOS;
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
	
	public Location getLocation(Location blockpos, String x, String y, String z){
		double worldX = 0, worldY = 0, worldZ = 0;
		if(x.contains("~")){
			x = x.replace("~", "");
			worldX += blockpos.getX();
		}
		if(y.contains("~")){
			y = y.replace("~", "");
			worldY += blockpos.getY();
		}
		if(z.contains("~")){
			z = z.replace("~", "");
			worldZ += blockpos.getZ();
		}
		if(!x.isEmpty())worldX += Double.parseDouble(x);
		if(!y.isEmpty())worldY += Double.parseDouble(y);
		if(!z.isEmpty())worldZ += Double.parseDouble(z);
		return new Location(blockpos.getWorld(), worldX, worldY, worldZ);
	}
	
	
}
