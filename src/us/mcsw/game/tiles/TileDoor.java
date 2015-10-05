package us.mcsw.game.tiles;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import us.mcsw.game.GameHandler;
import us.mcsw.game.sprites.Sprite;

public class TileDoor extends Tile {

	private static final long	serialVersionUID	= 1L;

	public TileDoor(Point p, TileType type) {
		super(p, type);
	}

	@Override
	public Image getImage() {
		Image img = super.getImage();
		ArrayList<Integer> sameRow = new ArrayList<>();
		for (Sprite s : GameHandler.getCurrentWorld().getSprites()) {
			if (s instanceof TileDoor && s.getY() == getY()) {
				sameRow.add((int) s.getX());
			}
		}

		int width = img.getWidth(null);
		for (int i : sameRow.toArray(new Integer[0])) {
			boolean remove = true;
			if (i > getX()) {
				for (int x = (int) getX(); x <= i; x += width) {
					if (!sameRow.contains(Integer.valueOf(x))) {
						break;
					}
					if (x == i) {
						remove = false;
					}
				}
			} else if (i == getX()) {
				remove = false;
			} else {
				for (int x = (int) getX(); x >= i; x -= width) {
					if (!sameRow.contains(Integer.valueOf(x))) {
						break;
					}
					if (x == i) {
						remove = false;
					}
				}
			}
			if (remove) {
				sameRow.remove(Integer.valueOf(i));
			}
		}
		Collections.sort(sameRow);

		int index = sameRow.indexOf((int) getX());

		if (index % 2 == 1) {
			img = flipImage(img);
		}

		return img;
	}
}
