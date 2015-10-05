package us.mcsw.game.worlds;

import java.awt.Point;
import java.util.Random;

import us.mcsw.game.GameHandler;
import us.mcsw.game.sprites.Portal;
import us.mcsw.game.tiles.Tile;
import us.mcsw.game.tiles.TileLootChest;
import us.mcsw.game.tiles.TileType;

public class WildernessWorld extends OutsideWorld {

	private static final long	serialVersionUID	= 1L;

	public WildernessWorld(int level, int exitPortalPos) {
		super("Wilderness", "background_tile_grass", level, exitPortalPos);
	}

	@Override
	public void generate(Random rand) {
		super.generate(rand);

		if (level >= 5 && level <= 15 && rand.nextInt(16 - level) == 0) {
			int loc = -1;
			while (loc == -1 || loc == exitPortalPos
					|| loc == GameHandler.WORLD_START_POSITIONS.length - (exitPortalPos + 1)) {
				loc = rand.nextInt(GameHandler.WORLD_START_POSITIONS.length);
			}

			Portal ice = new Portal(GameHandler.WORLD_START_POSITIONS[GameHandler.WORLD_START_POSITIONS.length
					- (loc + 1)], GameHandler.WORLD_START_POSITIONS[loc], IceWorld.class, level + 1, loc);
			addSprite(ice);
		}

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
						Tile t = new Tile(new Point(x, y), TileType.LEAVES);
						addSprite(t);
					}
				}
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
}
