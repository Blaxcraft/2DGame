package us.mcsw.game.inv;

import java.awt.Color;
import java.io.Serializable;

import us.mcsw.game.GameHandler;
import us.mcsw.game.player.HealCause;
import us.mcsw.game.player.Stats;
import us.mcsw.game.sprites.MainCharacter;
import us.mcsw.game.worlds.WorldID;

public class Material implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private static final Color	GREEN				= new Color(0, 255, 0);

	public static final Material	POTION	= new Material("items/potion", GREEN), BOOTS = new Material("items/boots",
													GREEN), POUCH = new Material("items/pouch", GREEN);

	String							path;
	public Color					replace	= null;

	public Material(String path) {
		this.path = path;
	}

	public Material(String path, Color replace) {
		this.path = path;
		this.replace = replace;
	}

	public boolean onUse(MainCharacter mc, Item it) {
		if (this == POTION) {
			if (it.displayName.contains("Retreat")) {
				if (!GameHandler.currentWorld.equals(WorldID.getBaseId())) {
					mc.travelTo = WorldID.getBaseId();
					return true;
				} else
					return false;
			} else if (it.displayName.contains("Healing")) {
				if (Stats.Health.getBoost() >= 0) {
					return false;
				}
				Stats.healPlayer(it.displayName.contains("Minor") ? 25 : it.displayName.contains("Major") ? 100 : 50,
						HealCause.POTION);
				return true;
			}
		}
		if (this == POUCH) {
			if (it.displayName.contains("Holding")) {
				mc.inv.size += it.displayName.contains("Massive") ? 3 : it.displayName.contains("Greater") ? 2 : 1;
			}
			return true;
		}
		return false;
	}

	public String getFullPath() {
		return "images/" + path;
	}

}
