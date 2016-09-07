package richbar.com.github.commandplot.command;

import net.minecraft.server.v1_10_R1.*;

/**
 * Created by Rich Y on 07.09.2016.
 */
public enum Commands{

    BLOCKDATA(new CommandBlockData(),ElemType.COORDS, ElemType.REST),
    CLEAR	(new CommandClear(), ElemType.PLAYER, ElemType.REST),
    CLONE	(new CommandClone(), ElemType.COORDS, ElemType.COORDS, ElemType.DCOORDS, ElemType.REST),
    EFFECT	(new CommandEffect(), ElemType.ENTITY, ElemType.REST),
    ENCHANT	(new CommandEnchant(), ElemType.PLAYER, ElemType.REST),
    ENTITYDATA(new CommandEntityData(),ElemType.ENTITY, ElemType.REST),
    EXECUTE(new CommandExecute(), ElemType.ENTITY, ElemType.DCOORDS, ElemType.COMMAND),
    FILL	(new CommandFill(), ElemType.COORDS, ElemType.COORDS, ElemType.REST),
    GAMEMODE(new CommandGamemode(), ElemType.ARG, ElemType.PLAYER),
    GIVE	(new CommandGive(), ElemType.PLAYER, ElemType.ARG, ElemType.REST),
    KILL	(new CommandKill(), ElemType.ENTITY),
    PARTICLE(new CommandParticle(), ElemType.ARG, ElemType.COORDS, ElemType.DCOORDS, ElemType.REST),
    PLAYSOUND(new CommandPlaySound(),ElemType.ARG, ElemType.ARG, ElemType.PLAYER, ElemType.COORDS, ElemType.MAX2, ElemType.MAX2, ElemType.MAX1),
    REPLACEITEM(new CommandReplaceItem(), ElemType.ARG, ElemType.ENTITYorCOORD, ElemType.REST),
    REPLACEITEMCOORD(new CommandReplaceItem(), ElemType.ARG, ElemType.COORDS, ElemType.REST),
    SAY		(new CommandSay(), ElemType.REST),

    //STATS(...),

    SETBLOCK(new CommandSetBlock(), ElemType.COORDS, ElemType.REST),
    SPREADPLAYERS(new CommandSpreadPlayers(), ElemType.DCOORDS, ElemType.REST),
    STOPSOUND(new CommandStopSound(), ElemType.PLAYER, ElemType.REST),
    SUMMON	(new CommandSummon(), ElemType.MOB, ElemType.COORDS, ElemType.REST),
    TELL	(new CommandTell(), ElemType.PLAYER, ElemType.REST),
    TELLRAW	(new CommandTellRaw(), ElemType.PLAYER, ElemType.REST),
    TESTFOR	(new CommandTestFor(), ElemType.PLAYER, ElemType.REST),
    TESTFORBLOCK(new CommandTestForBlock(), ElemType.COORDS, ElemType.REST),
    TESTFORBLOCKS(new CommandTestForBlocks(), ElemType.COORDS, ElemType.COORDS, ElemType.REST),
    TITLE	(new CommandTitle(), ElemType.PLAYER, ElemType.REST),
    TP		(new CommandTp(), ElemType.PLAYER, ElemType.ENTITYorCOORD),
    TPCOORD	(new CommandTp(), ElemType.PLAYER, ElemType.COORDS),

    //TRIGGER (new CommandTrigger(), ElemType.REST),

    XP		(new CommandXp(), ElemType.ARG, ElemType.PLAYER);

    CommandAbstract id;
    private ElemType[] elements;
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

