package us.mcsw.game.worlds;

import java.io.Serializable;
import java.util.Random;

import us.mcsw.game.GameHandler;

public class WorldID implements Serializable {

	private static final long	serialVersionUID	= 1L;

	public String				name;
	public String				identifier;

	transient World				world				= null;

	public World getWorld() {
		return world;
	}

	public void setWorld(World w) {
		this.world = w;
	}

	WorldID(String name, long seed, World world) {
		this.name = name;
		identifier = getRandom(16, seed);
		this.world = world;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WorldID) {
			return ((WorldID) obj).identifier.equals(identifier);
		}
		return super.equals(obj);
	}

	public static WorldID getFromWorld(World w) {
		if (w instanceof BaseWorld && baseId != null) {
			return getBaseId();
		}
		WorldID id = new WorldID(w.name, w.seed(), w);
		return id;
	}

	static WorldID	baseId	= null;

	public static WorldID getBaseId() {
		if (baseId == null) {
			WorldID id = new WorldID("Base", 0, null);
			if (GameHandler.getWorld(id) == null) {
				World base = new BaseWorld();
				id = getFromWorld(base);
			}
			baseId = id;
		}
		return baseId;
	}

	static String	charand	= "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";

	private static String getRandom(int length, long seed) {
		Random rand = new Random(seed);
		char[] c = new char[length];
		for (int i = 0; i < c.length; i++) {
			c[i] = charand.charAt(rand.nextInt(charand.length()));
		}
		return new String(c);
	}

}
