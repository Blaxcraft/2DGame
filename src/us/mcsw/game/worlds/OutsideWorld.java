package us.mcsw.game.worlds;

import java.awt.Point;

import us.mcsw.game.GameHandler;
import us.mcsw.game.sprites.MainCharacter;
import us.mcsw.game.sprites.Portal;
import us.mcsw.game.sprites.Sprite;

public class OutsideWorld extends World {

	private static final long	serialVersionUID	= 1L;

	public int					level, exitPortalPos;

	public OutsideWorld(String name, String path, int level, int exitPortalPos) {
		super(name + " (Level " + level + ")", path);
		this.level = level;
		this.exitPortalPos = exitPortalPos;
	}

	@Override
	public long seed() {
		long seed = 1;
		for (char c : name.toCharArray()) {
			seed += c;
		}
		seed += exitPortalPos + 10;
		seed += level * (level + 1);
		return seed;
	}

	@Override
	public void onPlayerEnter(MainCharacter mc) {
		super.onPlayerEnter(mc);

		int cind = 0;
		for (int i = 0; i < GameHandler.WORLD_START_POSITIONS.length; i++) {
			if (mc.getX() == GameHandler.WORLD_START_POSITIONS[i].x
					&& mc.getY() == GameHandler.WORLD_START_POSITIONS[i].y) {
				cind = i;
				exitPortalPos = cind;
			}
		}

		int pind = GameHandler.WORLD_START_POSITIONS.length - (cind + 1);
		Point portalPos = GameHandler.WORLD_START_POSITIONS[pind];
		boolean portalExists = false;
		for (Sprite s : getSprites()) {
			if (s instanceof Portal && s.getX() == portalPos.x && s.getY() == portalPos.y) {
				portalExists = true;
			}
		}
		if (!portalExists) {
			addSprite(new Portal(portalPos, GameHandler.WORLD_START_POSITIONS[GameHandler.WORLD_START_POSITIONS.length
					- (pind + 1)], getClass(), level + 1, pind));
		}
	}

}
