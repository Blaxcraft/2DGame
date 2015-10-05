package us.mcsw.game.inv;

import java.awt.Color;

import us.mcsw.game.tiles.TileType;

public class Items {

	// Bland
	public static final Item	WOOD				= TileType.WOOD.getItem();

	// Common
	public static final Item	DOOR				= TileType.DOOR.getItem();
	public static final Item	TRAPDOOR			= TileType.TRAPDOOR.getItem();

	// Uncommon

	// Rare
	public static final Item	BOOTS_OF_SPEED		= new Item(Material.BOOTS, ItemType.BOOTS, "Boots of Speed",
															Rarity.Rare, new Color(80, 80, 180), "+30 Speed");
	public static final Item	POUCH_OF_HOLDING1	= new Item(Material.POUCH, ItemType.CONSUMABLE, "Pouch of Holding",
															Rarity.Rare, new Color(255, 100, 255), "+1 Inventory Slot");

	// Exotic

	// Epic
	public static final Item	POUCH_OF_HOLDING2	= new Item(Material.POUCH, ItemType.CONSUMABLE,
															"Pouch of Greater Holding", Rarity.Epic, new Color(255,
																	255, 100), "+2 Inventory Slots");

	// Legendary
	
	// Godly

	// Relic
	public static final Item	POUCH_OF_HOLDING3	= new Item(Material.POUCH, ItemType.CONSUMABLE,
															"Pouch of Massive Holding", Rarity.Relic, new Color(100,
																	255, 255), "+3 Inventory Slots");

	// Artifact
	public static final Item	BOOTS_OF_ALACRITY	= new Item(Material.BOOTS, ItemType.BOOTS, "Boots of Alacrity",
															Rarity.Artifact, new Color(200, 80, 80), "+100 Speed",
															"+10 Defense");

	// Potions
	public static final Item	POTION_OF_RETREAT	= potion("Potion of Retreat", Rarity.Epic,
															new Color(255, 120, 255), "Teleports you home");
	public static final Item	POTION_OF_HEALTH1	= potion("Potion of Minor Healing", Rarity.Common, new Color(255,
															125, 125), "Heals 25 health");
	public static final Item	POTION_OF_HEALTH2	= potion("Potion of Healing", Rarity.Rare, new Color(255, 75, 75),
															"Heals 50 health");
	public static final Item	POTION_OF_HEALTH3	= potion("Potion of Major Healing", Rarity.Legendary, new Color(
															255, 25, 25), "Heals 100 health");

	private static final Item potion(String name, Rarity r, Color c, String... desc) {
		return new Item(Material.POTION, ItemType.CONSUMABLE, name, r, c, desc);
	}

}
