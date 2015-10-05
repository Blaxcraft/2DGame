package us.mcsw.game.inv;

import java.util.ArrayList;
import java.util.List;

public enum ItemType {
	
	DEFAULT,
	CONSUMABLE,
	BLOCK,
	HELMET(Inventory.MAX_SIZE),
	CHESTPIECE(Inventory.MAX_SIZE + 2),
	GREAVES(Inventory.MAX_SIZE + 4),
	BOOTS(Inventory.MAX_SIZE + 6),
	AMULET(Inventory.MAX_SIZE + 1),
	RING(Inventory.MAX_SIZE + 3, Inventory.MAX_SIZE + 5),
	ANKLET(Inventory.MAX_SIZE + 7);
	
	public List<Integer> validSlots;
	
	private ItemType(int... slots) {
		validSlots = new ArrayList<>();
		for (int i = 0; i < Inventory.MAX_SIZE; i++) {
			validSlots.add(i);
		}
		for (int i : slots) {
			validSlots.add(i);
		}
	}

}
