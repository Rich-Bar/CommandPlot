package richbar.com.github.plotfix;

import net.minecraft.server.v1_9_R1.TileEntityCommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftBlockState;

public class FixCommandBlock extends CraftBlockState implements CommandBlock {
	
	private final FixCommandEntity commandBlock;
	private String command;
	private String name;

	public FixCommandBlock(Block block) {
		super(block);

		CraftWorld world = (CraftWorld) block.getWorld();
		this.commandBlock = ((FixCommandEntity) world.getTileEntityAt(getX(),
				getY(), getZ()));
		this.command = this.commandBlock.getCommandBlock().getCommand();
		this.name = this.commandBlock.getCommandBlock().getName();
	}

	public FixCommandBlock(Material material, FixCommandEntity te) {
		super(material);
		this.commandBlock = te;
		this.command = this.commandBlock.getCommandBlock().getCommand();
		this.name = this.commandBlock.getCommandBlock().getName();
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = ((command != null) ? command : "");
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = ((name != null) ? name : "@");
	}

	public boolean update(boolean force, boolean applyPhysics) {
		boolean result = super.update(force, applyPhysics);

		if (result) {
			this.commandBlock.getCommandBlock().setCommand(this.command);
			this.commandBlock.getCommandBlock().setName(this.name);
		}

		return result;
	}

	public TileEntityCommand getTileEntity() {
		return this.commandBlock;
	}
}