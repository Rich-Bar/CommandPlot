package richbar.com.github.plotfix.commands;import java.util.Collections;
import java.util.List;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.ChatMessage;
import net.minecraft.server.v1_9_R1.CommandException;
import net.minecraft.server.v1_9_R1.ExceptionUsage;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.ICommandListener;
import net.minecraft.server.v1_9_R1.MinecraftServer;

public class CommandSay extends CommandAbstract {

    public CommandSay() {}

    public String getCommand() {
        return "say";
    }

    public int a() {
        return 1;
    }

    public String getUsage(ICommandListener icommandlistener) {
        return "commands.say.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 0 && astring[0].length() > 0) {
            IChatBaseComponent ichatbasecomponent = b(icommandlistener, astring, 0, true);

            minecraftserver.getPlayerList().sendMessage(new ChatMessage("chat.type.announcement", new Object[] { icommandlistener.getScoreboardDisplayName(), ichatbasecomponent}));
        } else {
            throw new ExceptionUsage("commands.say.usage", new Object[0]);
        }
    }

    public List<String> tabComplete(MinecraftServer minecraftserver, ICommandListener icommandlistener, String[] astring, BlockPosition blockposition) {
        return astring.length >= 1 ? a(astring, minecraftserver.getPlayers()) : Collections.emptyList();
    }
}
