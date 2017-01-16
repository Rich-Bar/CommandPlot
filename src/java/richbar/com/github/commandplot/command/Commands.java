package richbar.com.github.commandplot.command;

import net.minecraft.server.v1_10_R1.*;
import org.bukkit.command.CommandExecutor;
import richbar.com.github.commandplot.scoreboard.ScoreboardFix;

public enum Commands{

    BLOCKDATA       (new CommandBlockData(),ElemType.COORDS),
    CLEAR	        (new CommandClear(), ElemType.PLAYER),
    CLONE	        (new CommandClone(), ElemType.COORDS, ElemType.COORDS, ElemType.RELCOORDS),
    EFFECT	        (new CommandEffect(), ElemType.ENTITY),
    ENCHANT	        (new CommandEnchant(), ElemType.PLAYER),
    ENTITYDATA      (new CommandEntityData(),ElemType.ENTITY),
    EXECUTE         (new CommandExecute(), ElemType.ENTITY, ElemType.RELCOORDS, ElemType.COMMAND),
    FILL	        (new CommandFill(), ElemType.COORDS, ElemType.COORDS),
    GAMEMODE        (new CommandGamemode(), ElemType.ARG, ElemType.PLAYER),
    GIVE	        (new CommandGive(), ElemType.PLAYER, ElemType.ARG),
    KILL	        (new CommandKill(), ElemType.ENTITY),
    PARTICLE        (new CommandParticle(), ElemType.ARG, ElemType.COORDS, ElemType.RELCOORDS),
    PLAYSOUND       (new CommandPlaySound(),ElemType.ARG, ElemType.ARG, ElemType.PLAYER, ElemType.COORDS, ElemType.MAX2, ElemType.MAX2, ElemType.MAX1),
    REPLACEITEM     (new CommandReplaceItem(), ElemType.ARG, ElemType.ENTITYorCOORD),
    REPLACEITEMCOORD(new CommandReplaceItem(), ElemType.ARG, ElemType.COORDS),
    SAY		        (new CommandSay()),

    //STATS(...),

    SETBLOCK        (new CommandSetBlock(), ElemType.COORDS),
    SPREADPLAYERS   (new CommandSpreadPlayers(), ElemType.RELCOORDS),
    STOPSOUND       (new CommandStopSound(), ElemType.PLAYER),
    SUMMON	        (new CommandSummon(), ElemType.MOB, ElemType.COORDS),
    TELL	        (new CommandTell(), ElemType.PLAYER),
    TELLRAW	        (new CommandTellRaw(), ElemType.PLAYER),
    TESTFOR	        (new CommandTestFor(), ElemType.PLAYER),
    TESTFORBLOCK    (new CommandTestForBlock(), ElemType.COORDS),
    TESTFORBLOCKS   (new CommandTestForBlocks(), ElemType.COORDS, ElemType.COORDS),
    TITLE	        (new CommandTitle(), ElemType.PLAYER),
    TP		        (new CommandTp(), ElemType.PLAYER, ElemType.ENTITYorCOORD),
    TPCOORD	        (new CommandTp(), ElemType.PLAYER, ElemType.COORDS),

    //TRIGGER       (new CommandTrigger(), ElemType.REST),

    XP		        (new CommandXp(), ElemType.ARG, ElemType.PLAYER);

    final CommandAbstract id;
    private final ElemType[] elements;
    Commands(CommandAbstract typeID, ElemType... elements) {
        this.elements = elements;
        id = typeID;
    }
    
    public ElemType[] getElements(){
        return elements;
    }

    public CommandAbstract getInst(){
        return id;
    }

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
                case RELCOORDS:
                    res += 3;
                default:
                    break;
            }
        }
        return res;
    }
}

