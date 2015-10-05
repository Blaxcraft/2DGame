package us.mcsw.game.inv;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import us.mcsw.game.GameHandler;
import us.mcsw.game.sprites.Sprite;

public class ItemDrop extends Sprite {

	private static final long	serialVersionUID	= 1L;

	public Item					it;

	public int					nopickup			= 50;

	public ItemDrop(Point p, Item it) {
		super(p, it.path);

		this.it = it;
		this.scale = 0.5;

		this.colorReplace = it.colorReplace;
	}

	boolean	asc	= true;

	@Override
	public Image getImage() {
		if (scale > 0.8) {
			asc = false;
		}
		if (scale < 0.4) {
			asc = true;
		}
		scale += asc ? 0.01 : -0.01;
		return super.getImage();
	}

	@Override
	public void render(Graphics2D g, Image img) {
		GameHandler.mouseOverItems.put(getBounds(), it);
		super.render(g, img);
	}

	@Override
	public void tick() {
		super.tick();
		if (nopickup >= 1) {
			nopickup--;
		}
	}

}
