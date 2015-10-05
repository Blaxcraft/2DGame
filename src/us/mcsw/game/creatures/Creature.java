package us.mcsw.game.creatures;

import java.awt.Point;

import us.mcsw.game.sprites.Sprite;

public class Creature extends Sprite {

	private static final long	serialVersionUID	= 1L;

	public Creature(Point p, CreatureType type) {
		super(p, type.path);
	}

}
