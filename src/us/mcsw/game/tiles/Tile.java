package us.mcsw.game.tiles;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import us.mcsw.game.GameHandler;
import us.mcsw.game.sprites.Sprite;

public class Tile extends Sprite {

	private static final long		serialVersionUID	= 1L;

	public TileType					type;
	public transient BufferedImage	image;

	public Tile(Point p, TileType type) {
		super(p, type.getFullPath());
		this.type = type;
	}

	@Override
	public void setX(double x) {
		x -= x % 16;
		super.setX(x);
	}

	@Override
	public void setY(double y) {
		y -= y % 16;
		super.setY(y);
	}

	public static boolean isIntersectingTile(Rectangle rect) {
		for (Sprite s : GameHandler.getCurrentWorld().getSprites()) {
			if (s.getBounds().intersects(rect)) {
				if (s instanceof Tile) {
					Tile t = (Tile) s;
					if (!t.type.passThrough)
						return true;
				}
			}
		}
		return false;
	}

	public static boolean isInsideTile(Point p) {
		for (Sprite s : GameHandler.getCurrentWorld().getSprites()) {
			if (s.getBounds().contains(p)) {
				if (s instanceof Tile) {
					return true;
				}
			}
		}
		return false;
	}

}
