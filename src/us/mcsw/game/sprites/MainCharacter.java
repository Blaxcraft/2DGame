package us.mcsw.game.sprites;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import us.mcsw.game.GameHandler;
import us.mcsw.game.inv.Inventory;
import us.mcsw.game.inv.Item;
import us.mcsw.game.inv.ItemDrop;
import us.mcsw.game.inv.LootTable;
import us.mcsw.game.inv.Rarity;
import us.mcsw.game.player.DamageCause;
import us.mcsw.game.player.HealCause;
import us.mcsw.game.player.Stats;
import us.mcsw.game.tiles.Tile;
import us.mcsw.game.tiles.TileLootChest;
import us.mcsw.game.worlds.WorldID;

public class MainCharacter extends Sprite implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private double				dx					= 0;
	private double				dy					= 0;

	private boolean				u					= false, d = false, l = false, r = false;

	public boolean				flip				= false;

	public int					swingTime			= 0, swingCooldown = 0;

	public Portal				goingThrough		= null;
	public WorldID				travelTo			= null;

	public Inventory			inv;

	public long					healRate			= 1200;
	public long					millisUntilNextHeal	= healRate;

	public WorldID				saved_world_at		= null;

	private static final String	path				= "images/creatures/char";

	public MainCharacter(Point p) {
		super(p, path, 2);

		anim_rate = 10;
		inv = new Inventory(this);

		GameHandler.getCurrentWorld().onPlayerEnter(this);
	}

	public void update(long millis) {
		updateDir();

		Stats.calculateBoosts(inv);

		double speed = (double) millis / 5.0d;

		if (goingThrough != null || travelTo != null)
			speed = 0;

		double speedMult = (double) Stats.Speed.get() / 100.0D;
		speed *= speedMult;

		double velx = dx * speed;
		setX(getX() + velx);
		if (!GameHandler.bounds.contains(getBounds()) || Tile.isIntersectingTile(getBounds())) {
			setX(getX() - velx);
		}

		double vely = dy * speed;
		setY(getY() + vely);
		if (!GameHandler.bounds.contains(getBounds()) || Tile.isIntersectingTile(getBounds())) {
			setY(getY() - vely);
		}

		for (Sprite s : GameHandler.getCurrentWorld().getSprites()) {
			if (s.getBounds().intersects(getBounds())) {
				if (s instanceof ItemDrop) {
					ItemDrop id = (ItemDrop) s;
					if (id.nopickup <= 0 && inv.addItem(id.it)) {
						GameHandler.getCurrentWorld().removeSprite(id);
					}
				}
				if (s instanceof TileLootChest) {
					Rarity r = ((TileLootChest) s).rarity;
					Item i = LootTable.getChestDrop(r);
					ItemDrop drop = new ItemDrop(new Point((int) s.getX(), (int) s.getY()), i);
					GameHandler.getCurrentWorld().addSprite(drop);
					GameHandler.getCurrentWorld().removeSprite(s);
				}
			}
		}

		nextFrame();
		if (dx == 0 && dy == 0) {
			anim_index = 0;
		}

		if (swingTime >= 1) {
			anim_shown += 2;
		} else {
			anim_shown = anim_index;
		}

		if (swingTime > 0)
			swingTime -= millis;
		if (swingCooldown > 0)
			swingCooldown -= millis;

		if (goingThrough != null || travelTo != null) {
			scale -= (double) millis / 1000.0D;
			if (scale <= 0) {
				scale = 1;
				WorldID id = goingThrough != null ? goingThrough.worldId : travelTo;
				GameHandler.getCurrentWorld().onPlayerLeave(this);
				GameHandler.currentWorld = id;
				if (goingThrough != null) {
					setX(goingThrough.leave.x);
					setY(goingThrough.leave.y);
				} else {
					travelTo = null;
				}
				GameHandler.getWorld(id).onPlayerEnter(this);
				goingThrough = null;
				Stats.damagePlayer(10, DamageCause.MAGIC);
			}
		}

		millisUntilNextHeal -= millis;
		if (millisUntilNextHeal <= 0) {
			millisUntilNextHeal = healRate;
			Stats.healPlayer(1, HealCause.NATURAL_REGENERATION);
		}
	}

	public void updateDir() {
		double accelRate = 0.2;

		if (u && !d) {
			if (dy > -1) {
				dy -= accelRate;
			} else
				dy = -1;
		}
		if (d && !u) {
			if (dy < 1) {
				dy += accelRate;
			} else
				dy = 1;
		}
		if (l && !r) {
			if (dx > -1) {
				dx -= accelRate;
			} else
				dx = -1;
			flip = true;
		}
		if (r && !l) {
			if (dx < 1) {
				dx += accelRate;
			} else
				dx = 1;
			flip = false;
		}
		if (u == d) {
			if (dy > accelRate) {
				dy -= accelRate;
			} else if (dy < -accelRate) {
				dy += accelRate;
			} else
				dy = 0;
		}
		if (l == r) {
			if (dx > accelRate) {
				dx -= accelRate;
			} else if (dx < -accelRate) {
				dx += accelRate;
			} else
				dx = 0;
		}
	}

	@Override
	public void render(Graphics2D g, Image img) {
		super.render(g, img);

		for (Sprite s : GameHandler.getCurrentWorld().getSprites()) {
			if (s.getBounds().intersects(getBounds())) {
				if (s instanceof Portal) {
					Portal p = (Portal) s;

					String s1 = "Press E to go to", s2 = p.worldId.name;
					g.drawString(s1, (int) (realX - (g.getFontMetrics().stringWidth(s1) / 2) + (width / 2)),
							(int) (realY + height + 10));
					g.drawString(s2, (int) (realX - (g.getFontMetrics().stringWidth(s2) / 2) + (width / 2)),
							(int) (realY + height + 20));
				}
				if (s instanceof SpriteBase) {
					SpriteBase.renderCraftingMenu(g, inv);
				}
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_A) {
			l = true;
		} else if (key == KeyEvent.VK_D) {
			r = true;
		} else if (key == KeyEvent.VK_W) {
			u = true;
		} else if (key == KeyEvent.VK_S) {
			d = true;
		} else if (key == KeyEvent.VK_SPACE) {
			if (swingCooldown < 1) {
				swingTime = 400;
				swingCooldown = 700;
			}
		} else if (key == KeyEvent.VK_E) {
			if (!inv.onUse(this, GameHandler.mousePos)) {
				for (Sprite s : GameHandler.getCurrentWorld().getSprites()) {
					if (s.getBounds().intersects(getBounds()) && s instanceof Portal) {
						Portal p = (Portal) s;
						goingThrough = p;
					}
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_A) {
			l = false;
		} else if (key == KeyEvent.VK_D) {
			r = false;
		} else if (key == KeyEvent.VK_W) {
			u = false;
		} else if (key == KeyEvent.VK_S) {
			d = false;
		}
	}

}
