package richbar.com.github.commandplot.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArmorCalc {

	public static double getArmor(Player player){
		 org.bukkit.inventory.PlayerInventory inv = player.getInventory();
         ItemStack helmet, boots, chest, pants;
         double red = 0.0;

         if(inv.getBoots() != null){
        	 boots = inv.getBoots();
        	 if(boots != null){
		         if(boots.getType() == Material.LEATHER_BOOTS)red +=  0.04;
		         else if(boots.getType() == Material.GOLD_BOOTS)red +=  0.04;
		         else if(boots.getType() == Material.CHAINMAIL_BOOTS)red +=  0.04;
		         else if(boots.getType() == Material.IRON_BOOTS)red +=  0.08;
		         else if(boots.getType() == Material.DIAMOND_BOOTS)red +=  0.12;
        	 }
         }
         if (inv.getHelmet() != null){
        	 helmet = inv.getHelmet();
        	 if(helmet != null){
		         if(helmet.getType() == Material.LEATHER_HELMET)red +=  0.04;
		         else if(helmet.getType() == Material.GOLD_HELMET)red +=  0.08;
		         else if(helmet.getType() == Material.CHAINMAIL_HELMET)red +=  0.08;
		         else if(helmet.getType() == Material.IRON_HELMET)red +=  0.08;
		         else if(helmet.getType() == Material.DIAMOND_HELMET)red +=  0.12;
        	 }
         }
         if (inv.getChestplate() != null){
        	 chest = inv.getChestplate();
        	 if(chest != null){
		         if(chest.getType() == Material.LEATHER_CHESTPLATE)red +=  0.12;
		         else if(chest.getType() == Material.GOLD_CHESTPLATE)red +=  0.20;
		         else if(chest.getType() == Material.CHAINMAIL_CHESTPLATE)red +=  0.20;
		         else if(chest.getType() == Material.IRON_CHESTPLATE)red +=  0.24;
		         else if(chest.getType() == Material.DIAMOND_CHESTPLATE)red +=  0.32;
        	 }
         }
         if (inv.getLeggings() != null){
        	 pants = inv.getLeggings();
        	 if(pants != null){
	        	 if(pants.getType() == Material.LEATHER_LEGGINGS)red +=  0.08;
	             else if(pants.getType() == Material.GOLD_LEGGINGS)red +=  0.12;
	             else if(pants.getType() == Material.CHAINMAIL_LEGGINGS)red +=  0.16;
	             else if(pants.getType() == Material.IRON_LEGGINGS)red +=  0.20;
	             else if(pants.getType() == Material.DIAMOND_LEGGINGS)red +=  0.24;
        	 }
         }
         return red;
	}
}
