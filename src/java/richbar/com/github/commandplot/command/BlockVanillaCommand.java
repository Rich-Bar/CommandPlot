package richbar.com.github.commandplot.command;

import org.bukkit.command.CommandSender;

/**
 * Created by Rich Y on 02.12.2016.
 */
public class BlockVanillaCommand extends org.bukkit.command.defaults.VanillaCommand {
    protected BlockVanillaCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
