package richbar.com.github.plotfix.commands;import java.util.Collections;
import java.util.List;

import com.google.gson.JsonParseException;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.ChatComponentUtils;
import net.minecraft.server.v1_9_R1.CommandException;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.ExceptionUsage;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.ICommandListener;
import net.minecraft.server.v1_9_R1.MinecraftServer;

public class CommandTellRaw extends CommandAbstract {

    public CommandTellRaw() {}

    public String getCommand() {
        return "tellraw";
    }

    public int a() {
        return 2;
    }

    public String getUsage(ICommandListener icommandlistener) {
        return "commands.tellraw.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new ExceptionUsage("commands.tellraw.usage", new Object[0]);
        } else {
            EntityPlayer entityplayer = a(minecraftserver, icommandlistener, astring[0]);
            String s = a(astring, 1);

            try {
                IChatBaseComponent ichatbasecomponent = IChatBaseComponent.ChatSerializer.a(s);

                entityplayer.sendMessage(ChatComponentUtils.filterForDisplay(icommandlistener, ichatbasecomponent, entityplayer));
            } catch (JsonParseException jsonparseexception) {
                throw a(jsonparseexception);
            }
        }
    }

    public List<String> tabComplete(MinecraftServer minecraftserver, ICommandListener icommandlistener, String[] astring, BlockPosition blockposition) {
        return astring.length == 1 ? a(astring, minecraftserver.getPlayers()) : Collections.emptyList();
    }

    public boolean isListStart(String[] astring, int i) {
        return i == 0;
    }
}
