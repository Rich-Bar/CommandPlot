package richbar.com.github.commandplot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;

import org.bukkit.craftbukkit.v1_10_R1.command.VanillaCommandWrapper;
import net.minecraft.server.v1_10_R1.CommandAbstract;
import net.minecraft.server.v1_10_R1.CommandBlockData;
import net.minecraft.server.v1_10_R1.CommandClear;
import net.minecraft.server.v1_10_R1.CommandClone;
import net.minecraft.server.v1_10_R1.CommandEffect;
import net.minecraft.server.v1_10_R1.CommandEnchant;
import net.minecraft.server.v1_10_R1.CommandEntityData;
import net.minecraft.server.v1_10_R1.CommandExecute;
import net.minecraft.server.v1_10_R1.CommandFill;
import net.minecraft.server.v1_10_R1.CommandGamemode;
import net.minecraft.server.v1_10_R1.CommandGive;
import net.minecraft.server.v1_10_R1.CommandKill;
import net.minecraft.server.v1_10_R1.CommandParticle;
import net.minecraft.server.v1_10_R1.CommandPlaySound;
import net.minecraft.server.v1_10_R1.CommandReplaceItem;
import net.minecraft.server.v1_10_R1.CommandSay;
import net.minecraft.server.v1_10_R1.CommandSetBlock;
import net.minecraft.server.v1_10_R1.CommandSpreadPlayers;
import net.minecraft.server.v1_10_R1.CommandStopSound;
import net.minecraft.server.v1_10_R1.CommandSummon;
import net.minecraft.server.v1_10_R1.CommandTell;
import net.minecraft.server.v1_10_R1.CommandTellRaw;
import net.minecraft.server.v1_10_R1.CommandTestFor;
import net.minecraft.server.v1_10_R1.CommandTestForBlock;
import net.minecraft.server.v1_10_R1.CommandTestForBlocks;
import net.minecraft.server.v1_10_R1.CommandTitle;
import net.minecraft.server.v1_10_R1.CommandTp;
import net.minecraft.server.v1_10_R1.CommandXp;

import richbar.com.github.commandplot.api.PlotChecker;
import richbar.com.github.commandplot.caching.objects.PlotObject;
import richbar.com.github.commandplot.command.ExecuteSender;
import richbar.com.github.commandplot.command.TestForSender;
import richbar.com.github.commandplot.command.pipeline.SimpleCommandManager;
import richbar.com.github.commandplot.util.IsLocation;

public class CommandManager extends SimpleCommandManager{
	public enum Commands{

		BLOCKDATA(new CommandBlockData(),elemType.COORDS, elemType.REST),
		CLEAR	(new CommandClear(), elemType.PLAYER, elemType.REST),
		CLONE	(new CommandClone(), elemType.COORDS, elemType.COORDS, elemType.DCOORDS, elemType.REST),
		EFFECT	(new CommandEffect(), elemType.ENTITY, elemType.REST),
		ENCHANT	(new CommandEnchant(), elemType.PLAYER, elemType.REST),
		ENTITYDATA(new CommandEntityData(),elemType.ENTITY, elemType.REST),
		EXECUTE(new CommandExecute(), elemType.ENTITY, elemType.DCOORDS, elemType.COMMAND),
		FILL	(new CommandFill(), elemType.COORDS, elemType.COORDS, elemType.REST),
		GAMEMODE(new CommandGamemode(), elemType.ARG, elemType.PLAYER),
		GIVE	(new CommandGive(), elemType.PLAYER, elemType.ARG, elemType.REST),
		KILL	(new CommandKill(), elemType.ENTITY),
		PARTICLE(new CommandParticle(), elemType.ARG, elemType.COORDS, elemType.DCOORDS, elemType.REST),
		PLAYSOUND(new CommandPlaySound(),elemType.ARG, elemType.ARG, elemType.PLAYER, elemType.COORDS, elemType.MAX2, elemType.MAX2, elemType.MAX1),
		REPLACEITEM(new CommandReplaceItem(), elemType.ARG, elemType.ENTITYorCOORD, elemType.REST),
		REPLACEITEMCOORD(new CommandReplaceItem(), elemType.ARG, elemType.COORDS, elemType.REST),
		SAY		(new CommandSay(), elemType.REST),
		
		//SCOREBOARD(new CommandScoreboard(), elemType.REST), //TODO: Implement Scoreboard
		//STATS(...),
		
		SETBLOCK(new CommandSetBlock(), elemType.COORDS, elemType.REST),
		SPREADPLAYERS(new CommandSpreadPlayers(), elemType.DCOORDS, elemType.REST),
		STOPSOUND(new CommandStopSound(), elemType.PLAYER, elemType.REST),
		SUMMON	(new CommandSummon(), elemType.MOB, elemType.COORDS, elemType.REST),
		TELL	(new CommandTell(), elemType.PLAYER, elemType.REST),
		TELLRAW	(new CommandTellRaw(), elemType.PLAYER, elemType.REST),
		TESTFOR	(new CommandTestFor(), elemType.PLAYER, elemType.REST),
		TESTFORBLOCK(new CommandTestForBlock(), elemType.COORDS, elemType.REST),
		TESTFORBLOCKS(new CommandTestForBlocks(), elemType.COORDS, elemType.COORDS, elemType.REST),
		TITLE	(new CommandTitle(), elemType.PLAYER, elemType.REST),
		TP		(new CommandTp(), elemType.PLAYER, elemType.ENTITYorCOORD),
		TPCOORD	(new CommandTp(), elemType.PLAYER, elemType.COORDS),
		
		//TRIGGER (new CommandTrigger(), elemType.REST),
		
		XP		(new CommandXp(), elemType.ARG, elemType.PLAYER);
		
		CommandAbstract id;
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
	PlotChecker<?> checker;
	public CommandManager(CPlugin cPlugin, Command c) {
		super(c);
		main = cPlugin;
		checker = main.check;
	}

	@Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Location blockpos = new Location(main.getServer().getWorlds().get(0), 0, 0, 0, 0, 0);
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
        }else{
        	return onVanilla(sender, label, args);
        }
		
		Logger.getGlobal().warning(label + Arrays.toString(args));
		
		
		if(!(checker.isPlotWorld(blockpos.getWorld())))return onVanilla(sender, label, args);
		if(!main.getWhitelist().contains(label.toLowerCase()) || ((Plot)checker.getPlot(blockpos)) == null) return false;
		PlotId pId = ((Plot)checker.getPlot(blockpos)).getId();
		
		if(!(checker.canRun(blockpos) || main.activePlots.contains(new PlotObject(pId)))){
			sender.sendMessage("no Plotmember in plot nor always active!");
			return false;
		}
		
		if(!main.limiter.check(pId)){
			sender.sendMessage("too many executions!");
			return false;
		}
		
    	try{
    		Commands commandType = Commands.valueOf(label.toUpperCase());
    		
    		if(commandType.ordinal() == Commands.REPLACEITEM.ordinal() && args[0].equalsIgnoreCase("block")) 
    			commandType = Commands.REPLACEITEMCOORD;
        	if(commandType.ordinal() == Commands.TP.ordinal() && args.length > 2)
        		commandType = Commands.TPCOORD; 
    		
    		elemType[] elements = commandType.getElements();
    		
    		
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
    					
    					}else if(commandType.ordinal() == Commands.SPREADPLAYERS.ordinal()){
    						if(i <= 2) 
    							try{
    								if(Integer.parseInt(args[commandType.getIndex(i)]) > 256) 
    									invalidArgs.add(commandType.getIndex(i) + "");
	    						}catch(NumberFormatException nfe){
									invalidArgs.add(commandType.getIndex(i) + "");
	    						}
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
						else if(!checker.isSamePlot(blockpos, player.getLocation())) invalidArgs.add(commandType.getIndex(i)+ "");
						break;
						
					case ENTITYorCOORD:
					case ENTITY:
						Map<UUID, Object> uuidSet = getUUIDset(blockpos.getWorld().getEntities().toArray());
 						Object e  = uuidSet.get(args[commandType.getIndex(i)]);
						if(e == null){
							e = getPlayer(args[commandType.getIndex(i)]);
							if(e == null){
								invalidArgs.add(commandType.getIndex(i)+ "");
								break;
							}
						}
 						Location loca = e instanceof Entity? ((Entity) e).getLocation(): ((Player) e).getLocation();
 						artifacts.add(e);
						if(!checker.isSamePlot(blockpos, loca)) invalidArgs.add(commandType.getIndex(i)+ "");
						break;
						
						
					case COORDS:
						int indeX = commandType.getIndex(i), 
						indeY = indeX +1,
						indeZ = indeY +1;
						
						IsLocation nLoc;
						if(commandType.ordinal() == Commands.TPCOORD.ordinal())
							nLoc = new IsLocation(((Player) artifacts.get(0)).getLocation(), args[indeX], args[indeY], args[indeZ]);
						else
							nLoc = new IsLocation(blockpos, args[indeX], args[indeY], args[indeZ]);
						artifacts.add(nLoc);
						
						if(!checker.isSamePlot(blockpos, nLoc)) 
							invalidArgs.add(indeX + "");
						
						if(commandType.ordinal() == Commands.CLONE.ordinal()){
							prev = i == 0? nLoc : prev.subtract(nLoc.getX(), nLoc.getY(), nLoc.getZ());
						}
						break;
						
						
					case DCOORDS:
						indeX = commandType.getIndex(i);
						indeY = indeX +1;
						indeZ = indeY +1;
						
						if(commandType.ordinal() == Commands.SPREADPLAYERS.ordinal()){
							nLoc = new IsLocation(blockpos, args[indeX], "64", args[indeY]);
						}else if(commandType.ordinal() == Commands.EXECUTE.ordinal()){
							Player p = (Player) artifacts.get(0);
							if(p != null)nLoc = new IsLocation(p.getLocation(), args[indeX], args[indeY], args[indeZ]);
							else break;
						}else nLoc = new IsLocation(blockpos, args[indeX], args[indeY], args[indeZ]);
							
						
						artifacts.add(nLoc);
						
						if(commandType.ordinal() == Commands.CLONE.ordinal()){
							if(	!checker.isSamePlot(blockpos, nLoc) ||
								!checker.isSamePlot(nLoc, nLoc.add(prev.getX(), prev.getY(), prev.getZ())))
									invalidArgs.add(indeX + "");
	
						}else if(commandType.ordinal() == Commands.PARTICLE.ordinal()){
							if(nLoc.getX() > 16) args[indeX] = 16 + "";
							if(nLoc.getY() > 16) args[indeY] = 16 + "";
							if(nLoc.getZ() > 16) args[indeZ] = 16 + "";
						}
						break;
						
						
					case COMMAND:
						String detect = args[commandType.getIndex(i)];
						boolean hasDetect = false;
						if(detect.equalsIgnoreCase("detect")){
							//TODO: Detect
							hasDetect = true;
						}
						
						IsLocation artLoc = (IsLocation) artifacts.get(1);
						if(args.length < 10) hasDetect = false;
						Command newCommand = main.getCommand(args[hasDetect? 10 : 4]);
						
						if(newCommand == null){
							invalidArgs.add(detect);
							return false;
						}
						if(artLoc == null){
							invalidArgs.add("Invalid Location");
							return false;
						}
						
						sender = new ExecuteSender(sender, artLoc.clone());
						
						if(	!main.getWhitelist().contains(newCommand.getLabel().toLowerCase()) ||
							!checker.canRun(artLoc.clone())) return false;
						return execute(sender, detect, Arrays.copyOfRange(args, 5, args.length));
						
						
					case MAX1:
						double dm1 = Double.parseDouble(args[commandType.getIndex(i)]);
						if(dm1 > 1.00) dm1 = 1.00;
						else if(dm1 < 0.00) dm1 = 0.00;
						args[commandType.getIndex(i)] = dm1 +"";
						artifacts.add(dm1);
						break;
						
						
					case MAX2:
						double dm2 = Double.parseDouble(args[commandType.getIndex(i)]);
						if(dm2 > 2.00) dm2 = 2.00;
						else if(dm2 < 0.00) dm2 = 0.00;
						args[commandType.getIndex(i)] = dm2 +"";
						artifacts.add(dm2);
					default:break;
					
				}
    			i++;
    		}
    		if(invalidArgs.size() == 0){
    			main.limiter.add(pId);
    			if( commandType.ordinal() == Commands.TESTFOR.ordinal() || 
    				commandType.ordinal() ==  Commands.TESTFORBLOCK.ordinal() || 
    				commandType.ordinal() ==  Commands.TESTFORBLOCKS.ordinal()){
	    				
    					TestForSender checkSender = new TestForSender(sender);
	    				onVanilla(checkSender, label, args);
	    				return checkSender.isSuccess();
    			
    			}else 	return onVanilla(sender, label, args);
    		}
    		
    		sender.sendMessage(main.messages.getString("execution-failed"));
    		for(String f : invalidArgs) sender.sendMessage(f + ": " + args[Integer.parseInt(f)]);
    		return false;
    	}catch(IllegalArgumentException|NullPointerException exc){
    		Logger logger = main.getLogger();
    		logger.info(main.messages.getString("execution-exception"));
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
		return main.getServer().getPlayer(name);
	}
	
	public static Map<UUID, Object> getUUIDset(Object... es){
		Map<UUID, Object> res = new HashMap<>();
		for(Object e : es){
			if(e instanceof Entity)res.put(((Entity) e).getUniqueId(), e);
			else if(e instanceof Player)res.put(((Player) e).getUniqueId(), e);
		}
		return res;
	}
}
