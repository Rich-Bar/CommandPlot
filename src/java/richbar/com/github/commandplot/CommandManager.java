package richbar.com.github.commandplot;

import com.intellectualcrafters.plot.object.Plot;

import com.intellectualcrafters.plot.object.PlotPlayer;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import richbar.com.github.commandplot.command.Commands;
import richbar.com.github.commandplot.command.ElemType;
import richbar.com.github.commandplot.command.ExecuteSender;
import richbar.com.github.commandplot.command.TestForSender;
import richbar.com.github.commandplot.util.IsLocation;
import richbar.com.github.commandplot.util.Util;

import java.util.*;

public class CommandManager {


    private final CPlugin main;
    private final Plot plot;
	private final CommandRouter.PSChecker api;
	

    CommandManager(CPlugin cPlugin, Plot plot, CommandRouter.PSChecker api) {
        main = cPlugin;
        this.plot = plot;
        this.api = api;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
    	if(CPlugin.isDebug) System.out.println("Managing Command: " + label + " " + String.join(" ", args) + " - exec by " + sender.getName());
	    try {
        	Location blockpos = Util.getSenderLoc(sender);
            Commands commandType = Commands.valueOf(label.toUpperCase());

            switch (commandType) {
                case REPLACEITEM:
                    if (args[0].equalsIgnoreCase("block"))
                        commandType = Commands.REPLACEITEMCOORD;
                    break;
                case TP:
                    if (args.length > 2)
                        commandType = Commands.TPCOORD;
                    break;
                case SAY:
                    for (PlotPlayer p : plot.getPlayersInPlot())
                        Util.getPlayer(p.getName()).sendMessage(String.join(" ", args));
                    main.limiter.add(plot.getId());
                    return true;
            }

            int i = 0;
            Location prev = null;
            List<Object> artifacts = new ArrayList<>();
            List<String> invalidArgs = new ArrayList<>();

            ElemType[] elements = commandType.getElements();
            for (ElemType element : elements) {
                if (commandType.getIndex(i) >= args.length) break;
                switch (element) {
                    case ARG:
                        if (commandType.ordinal() == Commands.GIVE.ordinal() && args[1].replace(":", "").contains(":")) {
                            String tmp = args[1].substring(args[1].lastIndexOf(":"), args[1].length());
                            if (args.length == 4)
                                args = new String[]{args[0], args[1], args[2], tmp, args[3]};
                            else
                                args = new String[]{args[0], args[1], args[2], tmp};

                        } else if (commandType.ordinal() == Commands.SPREADPLAYERS.ordinal()) {
                            if (i <= 2)
                                try {
                                    if (Integer.parseInt(args[commandType.getIndex(i)]) > 256)
                                        invalidArgs.add(commandType.getIndex(i) + "");
                                } catch (NumberFormatException nfe) {
                                    invalidArgs.add(commandType.getIndex(i) + "");
                                }
                        }
                        break;


                    case MOB:
                        String requested = args[commandType.getIndex(i)];
                        artifacts.add(requested);
                        if (main.blacklistedMobs.contains(requested.toLowerCase()))
                            invalidArgs.add(commandType.getIndex(i) + "");
                        break;


                    case PLAYER:
                        Player player = Util.getPlayer(args[commandType.getIndex(i)]);
                        artifacts.add(player);
                        if (player == null) invalidArgs.add(commandType.getIndex(i) + "");
                        else if (!api.isSamePlot(blockpos, player.getLocation()))
                            invalidArgs.add(commandType.getIndex(i) + "");
                        break;

                    case ENTITYorCOORD:
                    case ENTITY:
	                    Entity e = Util.getPlayer(args[commandType.getIndex(i)]);
                        if (e == null) {
                        	try {
		                        Map<UUID, Entity> uuidSet = Util.getUUIDset(Util.getEntitiesInPlot(plot));
		                        e = uuidSet.get(UUID.fromString(args[commandType.getIndex(i)]));
	                        }catch(IllegalArgumentException exc){
                                if (CPlugin.isDebug)
                                    System.out.println("Could not find corresponding Entity to UUID [" + commandType.getIndex(i) + "]");
                                invalidArgs.add(commandType.getIndex(i) + "");
                                break;
                            }
                        }
                        Location loca = e.getLocation();
                        artifacts.add(e);
                        if (!api.isSamePlot(blockpos, loca)) {
                            if (CPlugin.isDebug) System.out.println("Targeted Entity not in same Plot!");
                            invalidArgs.add(commandType.getIndex(i) + "");
                        }
                        break;


                    case COORDS:
                        int indeX = commandType.getIndex(i),
                                indeY = indeX + 1,
                                indeZ = indeY + 1;

                        IsLocation nLoc;
                        if (commandType.ordinal() == Commands.TPCOORD.ordinal())
                            nLoc = new IsLocation(((Player) artifacts.get(0)).getLocation(), args[indeX], args[indeY], args[indeZ]);
                        else
                            nLoc = new IsLocation(blockpos, args[indeX], args[indeY], args[indeZ]);
                        artifacts.add(nLoc);

                        if (!api.isSamePlot(blockpos, nLoc))
                            invalidArgs.add(indeX + "");

                        if (commandType.ordinal() == Commands.CLONE.ordinal()) {
                            prev = i == 0 ? nLoc : prev.subtract(nLoc.getX(), nLoc.getY(), nLoc.getZ());
                        }
                        break;


                    case RELCOORDS:
                        indeX = commandType.getIndex(i);
                        indeY = indeX + 1;
                        indeZ = indeY + 1;

                        if (commandType.ordinal() == Commands.SPREADPLAYERS.ordinal()) {
                            nLoc = new IsLocation(blockpos, args[indeX], "64", args[indeY]);
                        } else if (commandType.ordinal() == Commands.EXECUTE.ordinal()) {
                            Entity p = (Entity) artifacts.get(0);
                            if (p != null)
                                nLoc = new IsLocation(p.getLocation(), args[indeX], args[indeY], args[indeZ]);
                            else break;
                        } else nLoc = new IsLocation(blockpos, args[indeX], args[indeY], args[indeZ]);


                        artifacts.add(nLoc);

                        if (commandType.ordinal() == Commands.CLONE.ordinal()) {
                            if (!api.isSamePlot(blockpos, nLoc) ||
                                    !api.isSamePlot(nLoc, nLoc.add(prev.getX(), prev.getY(), prev.getZ())))
                                invalidArgs.add(indeX + "");

                        } else if (commandType.ordinal() == Commands.PARTICLE.ordinal()) {
                            if (nLoc.getX() > 16) args[indeX] = 16 + "";
                            if (nLoc.getY() > 16) args[indeY] = 16 + "";
                            if (nLoc.getZ() > 16) args[indeZ] = 16 + "";
                        }
                        break;


                    case COMMAND:
                        String detect = args[commandType.getIndex(i)];
                        boolean hasDetect = false;
                        if (detect.equalsIgnoreCase("detect")) {
                            //TODO: Detect
                            hasDetect = true;
                        }

                        IsLocation artLoc = (IsLocation) artifacts.get(1);
                        if (args.length < 10) hasDetect = false;
                        Command newCommand = main.getCommand(args[hasDetect ? 10 : 4]);

                        if (newCommand == null) {
                            invalidArgs.add(detect);
                            return false;
                        }
                        if (artLoc == null) {
                            invalidArgs.add("Invalid Location");
                            return false;
                        }

                        sender = new ExecuteSender(sender, artLoc.clone());

                        if (!main.getWhitelist().contains(newCommand.getLabel().toLowerCase()) ||
                                !api.canRun(artLoc.clone())) return false;
                        return execute(sender, detect, Arrays.copyOfRange(args, hasDetect? 11 : 5, args.length));


                    case MAX1:
                        double dm1 = Double.parseDouble(args[commandType.getIndex(i)]);
                        if (dm1 > 1.00) dm1 = 1.00;
                        else if (dm1 < 0.00) dm1 = 0.00;
                        args[commandType.getIndex(i)] = dm1 + "";
                        artifacts.add(dm1);
                        break;


                    case MAX2:
                        double dm2 = Double.parseDouble(args[commandType.getIndex(i)]);
                        if (dm2 > 2.00) dm2 = 2.00;
                        else if (dm2 < 0.00) dm2 = 0.00;
                        args[commandType.getIndex(i)] = dm2 + "";
                        artifacts.add(dm2);
                    default:
                        break;

                }
                i++;
            }
            if (invalidArgs.size() == 0) {
                main.limiter.add(plot.getId());
                CommandWrapper wrapper = new CommandWrapper(commandType.name(), commandType.getInst());
                if (commandType.ordinal() == Commands.TESTFOR.ordinal() ||
                        commandType.ordinal() == Commands.TESTFORBLOCK.ordinal() ||
                        commandType.ordinal() == Commands.TESTFORBLOCKS.ordinal()) {

                    TestForSender checkSender = new TestForSender(sender);
	                wrapper.execute(checkSender, label, args);
                    return checkSender.isSuccess();

                } else return wrapper.execute(sender, label, args) ? true : false;
            }

            sender.sendMessage(main.messages.getErrorTypeString("execution-failed").getText());
            for (String f : invalidArgs)
	            sender.sendMessage(main.messages.getErrorFormat(f + ": " + args[Integer.parseInt(f)]).getText());
            return false;
        } catch (IllegalArgumentException | NullPointerException exc) {
            if (main.isDebug()) {
	            System.out.println(main.messages.getErrorTypeString("execution-exception").getText());
                exc.printStackTrace();
            }
            return false;
        }
    }
}
