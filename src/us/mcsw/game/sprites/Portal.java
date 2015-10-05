package us.mcsw.game.sprites;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import us.mcsw.game.GameHandler;
import us.mcsw.game.worlds.World;
import us.mcsw.game.worlds.WorldID;

public class Portal extends Sprite {

	private static final long		serialVersionUID	= 1L;

	// for serialization
	public Point					enter;
	public Class<? extends World>	worldType;
	public int						level;
	public int						exitPortalPos;

	public WorldID					worldId;
	public Point					leave;

	public Portal(Point enter, Point leave, WorldID worldId) {
		super(enter, "images/tiles/portal");

		this.worldId = worldId;
		this.leave = leave;

		this.enter = enter;
	}

	public Portal(Point enter, Point leave, Class<? extends World> worldType, int level, int exitPortalPos) {
		super(enter, "images/tiles/portal");

		this.worldId = GameHandler.generateNewWorld(worldType, level, exitPortalPos);
		this.leave = leave;

		this.enter = enter;
		this.worldType = worldType;
		this.level = level;
		this.exitPortalPos = exitPortalPos;
	}

	@Override
	public void render(Graphics2D g, Image img) {
		if (worldId != null) {
			GameHandler.mouseOverText.put(getBounds(), "Portal to " + worldId.name);
		} else {
			worldId = GameHandler.generateNewWorld(worldType, level, exitPortalPos);
		}
		super.render(g, img);
	}

}
