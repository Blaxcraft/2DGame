package us.mcsw.game.worlds;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import us.mcsw.game.GameHandler;
import us.mcsw.game.inv.Rarity;
import us.mcsw.game.sprites.SpriteBase;
import us.mcsw.game.sprites.Portal;
import us.mcsw.game.tiles.Tile;
import us.mcsw.game.tiles.TileLootChest;
import us.mcsw.game.tiles.TileType;

public class BaseWorld extends World {

	private static final long	serialVersionUID	= 1L;

	public SpriteBase			base				= null;

	public BaseWorld() {
		super("Base", "background_tile_grass");
	}

	@Override
	public long seed() {
		return 0;
	}

	@Override
	public void generate(Random rand) {
		super.generate(rand);

		base = new SpriteBase();
		addSprite(base);

		Point[] start = GameHandler.WORLD_START_POSITIONS;
		addSprite(new Portal(start[0], start[3], WildernessWorld.class, 1, 3));
		addSprite(new Portal(start[1], start[2], WildernessWorld.class, 1, 2));
		addSprite(new Portal(start[2], start[1], WildernessWorld.class, 1, 1));
		addSprite(new Portal(start[3], start[0], WildernessWorld.class, 1, 0));

		addSprite(new Portal(new Point(128, 128), start[0], IceWorld.class, 10, 0));

		Rectangle b = GameHandler.bounds;

		int boxSize = 12 * TileType.WOOD.getWidth();
		for (int x = 0; x < boxSize; x += TileType.WOOD.getWidth()) {
			for (int y = 0; y < boxSize; y += TileType.WOOD.getHeight()) {
				if (x == 0 || x == boxSize - TileType.WOOD.getWidth() || y == 0
						|| y == boxSize - TileType.WOOD.getHeight()) {
					addSprite(new Tile(new Point((b.width / 2) - (boxSize / 2) + x,
							((b.height / 2) - (boxSize / 2) + y)), TileType.WOOD));
				}
			}
		}

		for (int i = 1; i < Rarity.values().length; i++) {
			Rarity r = Rarity.values()[i];
			for (int j = 0; j < 5; j++) {
				TileLootChest chest = new TileLootChest(new Point((i + 15) * 16, (10 + j) * 16), r);
				addSprite(chest);
			}
		}
	}

}
