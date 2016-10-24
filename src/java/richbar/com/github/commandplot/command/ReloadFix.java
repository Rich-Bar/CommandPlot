package richbar.com.github.commandplot.command;

import org.bukkit.command.CommandSender;

import richbar.com.github.commandplot.command.pipeline.MapChanger;

public class ReloadFix extends org.bukkit.command.defaults.ReloadCommand{

	private final MapChanger main;
	
	public ReloadFix(MapChanger main) {
		super("reload");
		this.main = main;
	}
	
	@Override
	public boolean execute(CommandSender sender, String currentAlias,
			String[] args) {
		try {
			main.undo();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ignored) {}
		return super.execute(sender, currentAlias, args);
	}
}
