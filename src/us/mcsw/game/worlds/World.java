package us.mcsw.game.worlds;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import us.mcsw.game.GameHandler;
import us.mcsw.game.inv.Rarity;
import us.mcsw.game.sprites.MainCharacter;
import us.mcsw.game.sprites.Portal;
import us.mcsw.game.sprites.Sprite;

public abstract class World implements Serializable {
	
	// TODO improve serialization

	private static final long	serialVersionUID	= 1L;

	public String				backgroundPath;
	public String				name;

	private WorldID				wid;

	public boolean				generated			= false;

	public World(String name, String path) {
		this.name = name;
		this.backgroundPath = "/images/backgrounds/" + path + ".png";
	}

	public WorldID getId() {
		if (wid != null) {
			if (wid.world == null) {
				wid.world = this;
			}
			return wid;
		}
		wid = WorldID.getFromWorld(this);
		return wid;
	}

	public void generate(Random rand) {

	}

	public abstract long seed();

	private Portal	enterPortal	= null;

	public void onPlayerLeave(MainCharacter mc) {
		GameHandler.saveWorldToFile(getId());
	}

	public void onPlayerEnter(MainCharacter mc) {

		if (!generated) {
			generate(GameHandler.getRandom());
			generated = true;
		}

		// portal exit
		if (mc.goingThrough != null) {
			for (Sprite s : getSprites()) {
				if (s instanceof Portal && s.getX() == mc.getX() && s.getY() == mc.getY()) {
					return;
				}
			}
			if (enterPortal != null)
				removeSprite(enterPortal);
			enterPortal = new Portal(new Point((int) mc.getX(), (int) mc.getY()), new Point(
					(int) mc.goingThrough.getX(), (int) mc.goingThrough.getY()), mc.goingThrough.curWorld);
			addSprite(enterPortal);
		}
	}

	private ArrayList<Sprite>	sprites	= new ArrayList<>();

	public void addSprite(Sprite s) {
		sprites.add(s);
		s.curWorld = getId();
	}

	public void removeSprite(Sprite s) {
		sprites.remove(s);
		s.curWorld = null;
	}

	public void renderWorld(Graphics2D g) {
		for (Sprite s : sprites.toArray(new Sprite[0])) {
			s.render(g);
		}
	}

	public List<Sprite> getSprites() {
		return new ArrayList<>(sprites);
	}

	public Sprite getSpriteAt(Point p) {
		for (Sprite s : sprites) {
			if ((int) s.getX() == p.x && (int) s.getY() == p.y) {
				return s;
			}
		}
		return null;
	}

	// TODO
	public static Rarity getChestRarity(Class<? extends World> clazz, int level) {
		ArrayList<Rarity> possible = new ArrayList<>();

		if (clazz == WildernessWorld.class) {
			if (level <= 15) {
				possible.add(Rarity.Common);
			}
			if (betweeen(level, 10, 20)) {
				possible.add(Rarity.Uncommon);
			}
			if (level >= 20) {
				possible.add(Rarity.Rare);
			}
		}
		if (clazz == IceWorld.class) {
			if (level <= 20) {
				possible.add(Rarity.Uncommon);
			}
			if (betweeen(level, 15, 30)) {
				possible.add(Rarity.Rare);
			}
			if (level >= 25) {
				possible.add(Rarity.Exotic);
			}
		}

		double weight = 0;
		for (Rarity r : possible) {
			weight += r.randomWeight();
		}

		double randInd = weight * Math.random();
		for (Rarity r : possible) {
			randInd -= r.randomWeight();
			if (randInd <= 0) {
				return r;
			}
		}

		return Rarity.Common;
	}

	private static boolean betweeen(int num, int min, int max) {
		return num >= min && num <= max;
	}

}
