package richbar.com.github.commandplot.command;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class ExecuteSender implements CommandSender{

	private final CommandSender sender;
	private final Location location;
	public ExecuteSender(CommandSender origSender, Location loc) {
		location = loc;
		sender = origSender;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin paramPlugin) {
		return sender.addAttachment(paramPlugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin paramPlugin,
			int paramInt) {
		return sender.addAttachment(paramPlugin, paramInt);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin paramPlugin,
			String paramString, boolean paramBoolean) {
		return sender.addAttachment(paramPlugin, paramString, paramBoolean);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin paramPlugin,
			String paramString, boolean paramBoolean, int paramInt) {
		return sender.addAttachment(paramPlugin, paramString, paramBoolean, paramInt);
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return sender.getEffectivePermissions();
	}

	@Override
	public boolean hasPermission(String paramString) {
		return sender.hasPermission(paramString);
	}

	@Override
	public boolean hasPermission(Permission paramPermission) {
		return sender.hasPermission(paramPermission);
	}

	@Override
	public boolean isPermissionSet(String paramString) {
		return sender.isPermissionSet(paramString);
	}

	@Override
	public boolean isPermissionSet(Permission paramPermission) {
		return sender.isPermissionSet(paramPermission);
	}

	@Override
	public void recalculatePermissions() {
		sender.recalculatePermissions();
	}

	@Override
	public void removeAttachment(
			PermissionAttachment paramPermissionAttachment) {
		sender.removeAttachment(paramPermissionAttachment);
		
	}

	@Override
	public boolean isOp() {
		return sender.isOp();
	}

	@Override
	public void setOp(boolean paramBoolean) {
		sender.setOp(paramBoolean);
	}

	@Override
	public Server getServer() {
		return sender.getServer();
	}

	@Override
	public String getName() {
		return sender.getName();
	}

	@Override
	public void sendMessage(String paramString) {
		sender.sendMessage(paramString);
	}

	@Override
	public void sendMessage(String[] paramArrayOfString) {
		sender.sendMessage(paramArrayOfString);
	}

	public Location getLocation() {
		return location;
	}

}
