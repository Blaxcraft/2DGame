package us.mcsw.game.worlds;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import us.mcsw.game.GameHandler;
import us.mcsw.game.tiles.Tile;
import us.mcsw.game.tiles.TileLootChest;
import us.mcsw.game.tiles.TileType;

public class IceWorld extends OutsideWorld {
	
	private static final long	serialVersionUID	= 1L;

	public IceWorld(int level, int exitPortalPos) {
		super("Ice", "background_tile_ice", level, exitPortalPos);
	}

	@Override
	public void generate(Random rand) {
		super.generate(rand);

		// if (level >= 15 && level <= 25 && rand.nextInt(25 - level) == 0) {
		// int loc = -1;
		// while (loc == -1 || loc == exitPortalPos
		// || loc == GameHandler.WORLD_START_POSITIONS.length - (exitPortalPos +
		// 1)) {
		// loc = rand.nextInt(GameHandler.WORLD_START_POSITIONS.length);
		// }
		//
		// Portal ice = new
		// Portal(GameHandler.WORLD_START_POSITIONS[GameHandler.WORLD_START_POSITIONS.length
		// - (loc + 1)], GameHandler.WORLD_START_POSITIONS[loc], IceWorld.class,
		// level + 1, loc);
		// addSprite(ice); // TODO add level gen after ice
		// }

		int sep = rand.nextInt(3) + 3;
		int thrWid;
		int thrLoc;
		int sepC = sep;
		for (int yi = 3; yi < (GameHandler.bounds.height / 16) - 4; yi++) {
			int y = yi * 16;
			if (--sepC < 0) {
				sep = rand.nextInt(3) + 3;
				sepC = sep;
				thrWid = rand.nextInt(3) + 2;
				thrLoc = rand.nextInt((GameHandler.bounds.width / 16) - 20) + 10;
				for (int xi = 0; xi < GameHandler.bounds.width / 16; xi++) {
					int x = xi * 16;
					if (xi < thrLoc || xi > thrWid + thrLoc) {
						Tile t = new Tile(new Point(x, y), TileType.ICE);
						addSprite(t);
					}
				}
			}
		}

		int lakes = rand.nextInt(3) + 2;
		for (int i = 0; i < lakes; i++) {
			int xs = rand.nextInt((GameHandler.bounds.width / 16) - 10) + 5;
			int ys = rand.nextInt((GameHandler.bounds.height / 16) - 20) + 10;

			List<Point> set = lakeGen(new ArrayList<Point>(), new Point(xs * 16, ys * 16), rand.nextInt(3) + 2);
			for (Point p : set) {
				if (getSpriteAt(p) != null) {
					removeSprite(getSpriteAt(p));
				}
				Tile t = new Tile(p, TileType.FROZEN_LAKE);
				addSprite(t);
			}
		}

		for (int i = 0; i < rand.nextInt(3) + 2; i++) {
			int x = 0;
			int y = 0;
			do {
				x = rand.nextInt((GameHandler.bounds.width / 16) - 6) + 3;
				y = rand.nextInt((GameHandler.bounds.height / 16) - 10) + 5;
			} while (GameHandler.getCurrentWorld().getSpriteAt(new Point(x * 16, y * 16)) != null);

			TileLootChest chest = new TileLootChest(new Point(x * 16, y * 16), getChestRarity(getClass(), level));
			addSprite(chest);
		}
	}

	public List<Point> lakeGen(ArrayList<Point> list, Point start, int iter) {
		ArrayList<Point> ret = new ArrayList<>();
		for (Point p : list) {
			ret.add(p);
		}

		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				if (GameHandler.getRandom().nextInt(2) == 0) {
					Point add = new Point(start.x + (dx * 16), start.y + (dy * 16));
					if (iter > 0) {
						for (Point p : lakeGen(ret, add, iter - 1)) {
							if (!ret.contains(p)) {
								ret.add(p);
							}
						}
					} else {
						if (!ret.contains(add)) {
							ret.add(add);
						}
					}
				}
			}
		}

		return ret;
	}
}
