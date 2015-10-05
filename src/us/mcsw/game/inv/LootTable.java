package us.mcsw.game.inv;

import java.util.Random;

import us.mcsw.game.GameHandler;

public class LootTable {

	public static Object[][]	lootTable	= {
											// Bland
			{},

			// Common
			{ Items.DOOR, 5, Items.TRAPDOOR, 5 },

			// Uncommon
			{ Items.POTION_OF_HEALTH1, 5 },

			// Rare
			{ Items.POUCH_OF_HOLDING1, 5, Items.BOOTS_OF_SPEED, 1 },

			// Exotic
			{ Items.POTION_OF_HEALTH2, 5 },

			// Epic
			{ Items.POUCH_OF_HOLDING2, 5, Items.POTION_OF_RETREAT, 10 },

			// Legendary
			{ Items.POTION_OF_HEALTH3, 5 },

			// Godly
			{ Items.TRAPDOOR, 100 },

			// Relic
			{ Items.POUCH_OF_HOLDING3, 5 },

			// Artifact
			{ Items.BOOTS_OF_ALACRITY, 10 },

											};

	static Random				rand		= GameHandler.getRandom();

	public static Item getChestDrop(Rarity r) {
		Object[] items = lootTable[r.ordinal()];

		double totalWeight = 0;

		for (int i = 0; i < items.length - 1; i += 2) {
			double weight = (int) items[i + 1];
			totalWeight += weight;
		}

		totalWeight *= Math.random();

		for (int i = 0; i < items.length - 1; i += 2) {
			double weight = (int) items[i + 1];
			totalWeight -= weight;

			if (totalWeight < 0) {
				return (Item) items[i];
			}
		}

		return null;
	}

}
