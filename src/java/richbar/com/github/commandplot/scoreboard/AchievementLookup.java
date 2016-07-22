package richbar.com.github.commandplot.scoreboard;

import org.bukkit.Achievement;

public enum AchievementLookup {

	achievement(null),
	achievement_acquireIron(Achievement.ACQUIRE_IRON),
	achievement_bakeCake(Achievement.BAKE_CAKE),
	achievement_blazeRod(Achievement.GET_BLAZE_ROD),
	achievement_bookcase(Achievement.BOOKCASE),
	achievement_breedCow(Achievement.BREED_COW),
	achievement_buildBetterPickaxe(Achievement.BUILD_BETTER_PICKAXE),
	achievement_buildFurnace(Achievement.BUILD_FURNACE),
	achievement_buildHoe(Achievement.BUILD_HOE),
	achievement_buildPickaxe(Achievement.BUILD_PICKAXE),
	achievement_buildSword(Achievement.BUILD_SWORD),
	achievement_buildWorkBench(Achievement.BUILD_WORKBENCH),
	achievement_cookFish(Achievement.COOK_FISH),
	achievement_diamonds(Achievement.GET_DIAMONDS),
	achievement_diamondsToYou(Achievement.DIAMONDS_TO_YOU),
	achievement_enchantments(Achievement.ENCHANTMENTS),
	achievement_exploreAllBiomes(Achievement.EXPLORE_ALL_BIOMES),
	achievement_flyPig(Achievement.FLY_PIG),
	achievement_fullBeacon(Achievement.FULL_BEACON),
	achievement_ghast(Achievement.GHAST_RETURN),
	achievement_killCow(Achievement.KILL_COW),
	achievement_killEnemy(Achievement.KILL_ENEMY),
	achievement_killWither(Achievement.KILL_WITHER),
	achievement_makeBread(Achievement.MAKE_BREAD),
	achievement_mineWood(Achievement.MINE_WOOD),
	achievement_onARail(Achievement.ON_A_RAIL),
	achievement_openInventory(Achievement.OPEN_INVENTORY),
	achievement_overkill(Achievement.OVERKILL),
	achievement_overpowered(Achievement.OVERPOWERED),
	achievement_portal(Achievement.NETHER_PORTAL),
	achievement_potion(Achievement.BREW_POTION),
	achievement_snipeSkeleton(Achievement.SNIPE_SKELETON),
	achievement_spawnWither(Achievement.SPAWN_WITHER),
	achievement_theEnd(Achievement.END_PORTAL),
	achievement_theEnd2(Achievement.THE_END);
	
	private Achievement result;
	AchievementLookup(Achievement result){
		this.result = result;
	}
	
	public Achievement getAchievement(){
		return result;
	}
}
