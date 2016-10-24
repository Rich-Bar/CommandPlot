package richbar.com.github.commandplot.util;

import org.bukkit.Location;
import org.bukkit.World;

public class IsLocation extends Location{

		private boolean isRelative;
		
		public IsLocation(World world, double x, double y, double z, boolean isRelative) {
			super(world, x, y, z);
			this.isRelative = isRelative;
		}
		
		public IsLocation(Location loc, boolean isRelative) {
			super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
			this.isRelative = isRelative;
		}
		
		public IsLocation(Location blockpos, String x, String y, String z){
			super(blockpos.getWorld(), blockpos.getX(), blockpos.getY(), blockpos.getZ());
			try{
				double worldX = 0, worldY = 0, worldZ = 0;
				
				boolean isRelative = false;
				if(x.contains("~")){
					x = x.replace("~", "");
					worldX += blockpos.getX();
					isRelative = true;
				}
				if(y.contains("~")){
					y = y.replace("~", "");
					worldY += blockpos.getY();
					isRelative = true;
				}
				if(z.contains("~")){
					z = z.replace("~", "");
					worldZ += blockpos.getZ();
					isRelative = true;
				}
				
				if(!x.isEmpty())worldX += Double.parseDouble(x);
				if(!y.isEmpty())worldY += Double.parseDouble(y);
				if(!z.isEmpty())worldZ += Double.parseDouble(z);
				
				this.isRelative = isRelative;
				setX(worldX);
				setY(worldY);
				setZ(worldZ);
			}catch(Exception ignored){}
		}
	}