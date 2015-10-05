package us.mcsw.game.tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import us.mcsw.game.GameHandler;
import us.mcsw.game.inv.Rarity;

public class TileLootChest extends Tile {

	private static final long	serialVersionUID	= 1L;

	public Rarity				rarity;

	public TileLootChest(Point p, Rarity rarity) {
		super(p, TileType.LOOT_CHEST);
		this.rarity = rarity;
		replaceColor(new Color(0, 255, 0), rarity.color);
	}

	@Override
	public void render(Graphics2D g, Image img) {
		GameHandler.mouseOverText.put(getBounds(), rarity.name() + " Chest");
		super.render(g, img);
	}

}
