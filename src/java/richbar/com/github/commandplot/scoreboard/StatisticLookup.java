package richbar.com.github.commandplot.scoreboard;

import org.bukkit.Statistic;

public enum StatisticLookup {
	
	stat(null),
	stat_animalsBred(Statistic.ANIMALS_BRED),
	stat_armorCleaned(Statistic.ARMOR_CLEANED),
	stat_aviateOneCm(Statistic.AVIATE_ONE_CM),
	stat_bannerCleaned(Statistic.BANNER_CLEANED),
	stat_beaconInteraction(Statistic.BEACON_INTERACTION),
	stat_boatOneCm(Statistic.BOAT_ONE_CM),
	stat_brewingstandInteraction(Statistic.BREWINGSTAND_INTERACTION),
	stat_cakeSlicesEaten(Statistic.CAKE_SLICES_EATEN),
	stat_cauldronFilled(Statistic.CAULDRON_FILLED),
	stat_cauldronUsed(Statistic.CAULDRON_USED),
	stat_chestOpened(Statistic.CHEST_OPENED),
	stat_climbOneCm(Statistic.CLIMB_ONE_CM),
	stat_craftingTableInteraction(Statistic.CRAFTING_TABLE_INTERACTION),
	stat_crouchOneCm(Statistic.CROUCH_ONE_CM),
	stat_damageDealt(Statistic.DAMAGE_DEALT),
	stat_damageTaken(Statistic.DAMAGE_TAKEN),
	stat_deaths(Statistic.DEATHS),
	stat_dispenserInspected(Statistic.DISPENSER_INSPECTED),
	stat_diveOneCm(Statistic.DIVE_ONE_CM),
	stat_dropperInspected(Statistic.DROPPER_INSPECTED),
	stat_enderchestOpened(Statistic.ENDERCHEST_OPENED),
	stat_fallOneCm(Statistic.FALL_ONE_CM),
	stat_fishCaught(Statistic.FISH_CAUGHT),
	stat_flowerPotted(Statistic.FLOWER_POTTED),
	stat_flyOneCm(Statistic.FLY_ONE_CM),
	stat_furnaceInteraction(Statistic.FURNACE_INTERACTION),
	stat_hopperInspected(Statistic.HOPPER_INSPECTED),
	stat_horseOneCm(Statistic.HORSE_ONE_CM),
	stat_itemEnchanted(Statistic.ITEM_ENCHANTED),
	stat_jump(Statistic.JUMP),
	stat_junkFished(Statistic.JUNK_FISHED),
	stat_leaveGame(Statistic.LEAVE_GAME),
	stat_minecartOneCm(Statistic.MINECART_ONE_CM),
	stat_mobKills(Statistic.MOB_KILLS),
	stat_noteblockPlayed(Statistic.NOTEBLOCK_PLAYED),
	stat_noteblockTuned(Statistic.NOTEBLOCK_TUNED),
	stat_pigOneCm(Statistic.PIG_ONE_CM),
	stat_playerKills(Statistic.PLAYER_KILLS),
	stat_playOneMinute(Statistic.PLAY_ONE_TICK),
	stat_recordPlayed(Statistic.RECORD_PLAYED),
	stat_sneakTime(Statistic.SNEAK_TIME),
	stat_sprintOneCm(Statistic.SPRINT_ONE_CM),
	stat_swimOneCm(Statistic.SWIM_ONE_CM),
	stat_talkedToVillager(Statistic.TALKED_TO_VILLAGER),
	stat_timeSinceDeath(Statistic.TIME_SINCE_DEATH),
	stat_sleepInBed(Statistic.SLEEP_IN_BED),
	stat_tradedWithVillager(Statistic.TRADED_WITH_VILLAGER),
	stat_trappedChestTriggered(Statistic.TRAPPED_CHEST_TRIGGERED),
	stat_treasureFished(Statistic.TREASURE_FISHED),
	stat_walkOneCm(Statistic.WALK_ONE_CM),
	
	stat_(null),
	stat_craftItem(Statistic.CRAFT_ITEM),
	stat_useItem(Statistic.USE_ITEM),
	stat_breakItem(Statistic.BREAK_ITEM),
	stat_mineBlock(Statistic.MINE_BLOCK),
	stat_killEntity(Statistic.KILL_ENTITY),
	stat_pickup(Statistic.PICKUP),
	stat_drop(Statistic.DROP),
	stat_entityKilledBy(Statistic.ENTITY_KILLED_BY);
	
	private Statistic statistic;
	StatisticLookup(Statistic result){
		statistic = result;
	}
	
	public Statistic getStatistic(){
		return statistic;
	}
	
}
