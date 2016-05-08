
package richbar.com.github.plotfix;

import net.minecraft.server.v1_9_R1.*;

public class FixCommandEntity extends TileEntityCommand {
	private boolean a;
	private boolean f;
	private boolean g;
	private boolean h;
	private final FixCommandBlockListener i = new FixCommandBlockListener() {
		public BlockPosition getChunkCoordinates() {
			return FixCommandEntity.this.position;
		}

		public Vec3D d() {
			return new Vec3D(FixCommandEntity.this.position.getX() + 0.5D,
					FixCommandEntity.this.position.getY() + 0.5D,
					FixCommandEntity.this.position.getZ() + 0.5D);
		}

		public World getWorld() {
			return FixCommandEntity.this.getWorld();
		}

		public void setCommand(String s) {
			super.setCommand(s);
			FixCommandEntity.this.update();
		}

		public void i() {
			IBlockData iblockdata = FixCommandEntity.this.world
					.getType(FixCommandEntity.this.position);

			FixCommandEntity.this.getWorld().notify(
					FixCommandEntity.this.position, iblockdata, iblockdata, 3);
		}

		public Entity f() {
			return null;
		}

		public MinecraftServer h() {
			return FixCommandEntity.this.world.getMinecraftServer();
		}
	};

	public void save(NBTTagCompound nbttagcompound) {
		super.save(nbttagcompound);
		this.i.a(nbttagcompound);
		nbttagcompound.setBoolean("powered", d());
		nbttagcompound.setBoolean("conditionMet", g());
		nbttagcompound.setBoolean("auto", e());
	}

	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		this.i.b(nbttagcompound);
		a(nbttagcompound.getBoolean("powered"));
		c(nbttagcompound.getBoolean("conditionMet"));
		b(nbttagcompound.getBoolean("auto"));
	}

	public Packet<?> getUpdatePacket() {
		if (h()) {
			d(false);
			NBTTagCompound nbttagcompound = new NBTTagCompound();

			save(nbttagcompound);
			return new PacketPlayOutTileEntityData(this.position, 2,
					nbttagcompound);
		}
		return null;
	}

	public boolean isFilteredNBT() {
		return true;
	}

	public CommandBlockListenerAbstract getCommandBlock() {
		return this.i;
	}

	public CommandObjectiveExecutor c() {
		return this.i.o();
	}

	public void a(boolean flag) {
		this.a = flag;
	}

	public boolean d() {
		return this.a;
	}

	public boolean e() {
		return this.f;
	}

	public void b(boolean flag) {
		boolean flag1 = this.f;

		this.f = flag;
		if ((!(flag1)) && (flag) && (!(this.a)) && (this.world != null)
				&& (i() != Type.SEQUENCE)) {
			Block block = getBlock();

			if (block instanceof BlockCommand) {
				BlockPosition blockposition = getPosition();
				BlockCommand blockcommand = (BlockCommand) block;

				this.g = ((!(j())) || (blockcommand.e(this.world, blockposition,
						this.world.getType(blockposition))));
				this.world.a(blockposition, block, block.a(this.world));
				if (this.g)
					blockcommand.c(this.world, blockposition);
			}
		}
	}

	public boolean g() {
		return this.g;
	}

	public void c(boolean flag) {
		this.g = flag;
	}

	public boolean h() {
		return this.h;
	}

	public void d(boolean flag) {
		this.h = flag;
	}

	public Type i() {
		Block block = getBlock();

		return ((block == Blocks.dd) ? Type.SEQUENCE
				: (block == Blocks.dc) ? Type.AUTO
						: (block == Blocks.COMMAND_BLOCK) ? Type.REDSTONE
								: Type.REDSTONE);
	}

	public boolean j() {
		IBlockData iblockdata = this.world.getType(getPosition());

		return ((iblockdata.getBlock() instanceof BlockCommand)
				? ((Boolean) iblockdata.get(BlockCommand.b)).booleanValue()
				: false);
	}

	public void z() {
		this.e = null;
		super.z();
	}
}