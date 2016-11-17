package richbar.com.github.commandplot;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.command.VanillaCommandWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import richbar.com.github.commandplot.api.PlotChecker;
import richbar.com.github.commandplot.caching.objects.PlotObject;
import richbar.com.github.commandplot.command.Commands;
import richbar.com.github.commandplot.command.ElemType;
import richbar.com.github.commandplot.command.ExecuteSender;
import richbar.com.github.commandplot.command.TestForSender;
import richbar.com.github.commandplot.command.pipeline.SimpleCommandManager;
import richbar.com.github.commandplot.util.IsLocation;
import richbar.com.github.commandplot.util.Util;

import java.util.*;
import java.util.logging.Logger;

public class CommandManager extends SimpleCommandManager{

	
	
	private final CPlugin main;
	private final PlotChecker<?> checker;

	CommandManager(CPlugin cPlugin, Command c) {
		super(c);
		main = cPlugin;
		checker = main.check;
	}

	@Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Location blockpos;
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
			if(!((Plot)checker.getPlot(blockpos)).getMembers().contains(((Player) sender).getUniqueId())) return false;
        }else{
        	return onVanilla(sender, label, args);
        }
		
		if(main.isDebug()) Logger.getGlobal().warning(label + Arrays.toString(args));
		Plot plot = ((Plot)checker.getPlot(blockpos));

		if(!main.getWhitelist().contains(label.toLowerCase()) || plot == null) return false;

		if(!(checker.isPlotWorld(blockpos.getWorld())))return onVanilla(sender, label, args);
		PlotId pId = plot.getId();

		if(!(checker.canRun(blockpos) || main.activePlots.contains(new PlotObject(pId)))){
			sender.sendMessage("no Plotmember in plot nor is plot always active!");
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
    		
    		ElemType[] elements = commandType.getElements();
    		
    		
    		Location prev = null;
    		List<Object> artifacts = new ArrayList<>();
    		List<String> invalidArgs = new ArrayList<>();
    		
    		int i = 0;
    		for(ElemType element : elements){
    			if(element == ElemType.REST || commandType.getIndex(i) >= args.length) break;
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
						Player player = Util.getPlayer(args[commandType.getIndex(i)]);
						artifacts.add(player);
						if(player == null) invalidArgs.add(commandType.getIndex(i)+ "");
						else if(!checker.isSamePlot(blockpos, player.getLocation())) invalidArgs.add(commandType.getIndex(i)+ "");
						break;

					case ENTITYorCOORD:
					case ENTITY:
						Map<UUID, Object> uuidSet = Util.getUUIDset(blockpos.getWorld().getEntities().toArray());
 						Object e  = uuidSet.get(args[commandType.getIndex(i)]);
						if(e == null){
							e = Util.getPlayer(args[commandType.getIndex(i)]);
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
	
	private boolean onVanilla(CommandSender sender, String label, String... args)
	{		
    	try{
    		Commands commandType = Commands.valueOf(label.toUpperCase());
    		VanillaCommandWrapper wrap = new VanillaCommandWrapper(commandType.getInst());
    		return wrap.execute(sender, label, args);
    	}catch(IllegalArgumentException|NullPointerException exc){
    		return false;
    	}
	}
}
