package us.mcsw.game.inv;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import us.mcsw.game.GameHandler;
import us.mcsw.game.sprites.MainCharacter;
import us.mcsw.game.sprites.Sprite;
import us.mcsw.game.tiles.Tile;
import us.mcsw.game.tiles.TileType;

public class Inventory implements Serializable {

	private static final long			serialVersionUID	= 1L;

	public MainCharacter				owner;

	public int							size				= 18;
	public static final int				MAX_SIZE			= 54;
	public static final int				EQUIPMENT_SLOTS		= 8;
	public Item[]						contents			= new Item[MAX_SIZE + EQUIPMENT_SLOTS];

	public HashMap<Integer, Rectangle>	slots				= new HashMap<>();

	public static Item					onCursor			= null;
	public static int					onCursorSlot		= 0;
	public static Point					onCursorPos			= null;

	public Inventory(MainCharacter owner) {
		this.owner = owner;
	}

	public boolean addItem(Item it) {
		for (int i = 0; i < size; i++) {
			if (contents[i] != null) {
				continue;
			}
			try {
				contents[i] = (Item) it.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public boolean removeItem(Item it) {
		for (int i = 0; i < size; i++) {
			if (contents[i] != null && contents[i].similar(it)) {
				contents[i] = null;
				return true;
			}
		}
		return false;
	}

	public void render(Graphics2D g) {
		int x = GameHandler.bounds.x + GameHandler.bounds.width + 20;
		int y = GameHandler.bounds.y + 100;

		final int sizeX = 50;
		final int sizeY = 50;

		int itemsPerRow = 9;

		int emptyToRender = size;
		int spacesToRender = MAX_SIZE;

		slots.clear();
		int slot = 0;
		for (int i = 0; i < (MAX_SIZE / itemsPerRow) + (MAX_SIZE % itemsPerRow == 0 ? 0 : 1); i++) {
			x = GameHandler.bounds.x + GameHandler.bounds.width + 20;
			for (int j = 0; j < itemsPerRow && spacesToRender > 0; j++) {
				g.setPaint(Color.gray);
				if (emptyToRender > 0) {
					emptyToRender--;
					g.setPaint(new Color(200, 200, 200));
					g.fillRect(x, y, sizeX, sizeY);
					g.setPaint(Color.gray);
					g.drawRect(x, y, sizeX, sizeY);
				} else {
					g.fillRect(x, y, sizeX, sizeY);
				}
				g.setPaint(Color.black);
				if (contents[slot] != null) {
					Item it = contents[slot];
					g.drawImage(it.getImage(), x + ((sizeX - (int) it.width) / 2), y + ((sizeY - (int) it.height) / 2),
							null);
				}
				slots.put(slot++, new Rectangle(x, y, sizeX, sizeY));
				g.drawString(slot + "", x + (sizeX - g.getFontMetrics().stringWidth(slot + "") - 5), y + 12);
				x += sizeX + 10;
				spacesToRender--;
			}
			y += sizeY + 10;
		}

		itemsPerRow = 2;

		int toRender = EQUIPMENT_SLOTS;

		y = GameHandler.bounds.y + 100;

		for (int i = 0; i < (EQUIPMENT_SLOTS / itemsPerRow) + (EQUIPMENT_SLOTS % itemsPerRow == 0 ? 0 : 1); i++) {
			x = GameHandler.bounds.x + GameHandler.bounds.width + 570;
			for (int j = 0; j < itemsPerRow && toRender > 0; j++) {
				g.setPaint(Color.gray);
				g.drawRect(x, y, sizeX, sizeY);
				if (contents[slot] != null) {
					Item it = contents[slot];
					g.drawImage(it.getImage(), x + ((sizeX - (int) it.width) / 2), y + ((sizeY - (int) it.height) / 2),
							null);
				}
				slots.put(slot++, new Rectangle(x, y, sizeX, sizeY));
				g.setPaint(Color.black);
				x += sizeX + 10;
				spacesToRender--;
			}
			y += sizeY + 10;
		}

		if (onCursor != null) {
			onCursorPos = new Point(GameHandler.mousePos.x, GameHandler.mousePos.y);
			if (onCursor.type == ItemType.BLOCK) {
				onCursorPos.translate(-((onCursorPos.x - GameHandler.bounds.x) % 16),
						-((onCursorPos.y - GameHandler.bounds.y) % 16));
			}
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					GameHandler.bounds.contains(onCursorPos) ? 0.5f : 1f));
			g.drawImage(onCursor.getImage(), onCursorPos.x, onCursorPos.y, null);
			g.setComposite(AlphaComposite.Clear);
		} else {
			for (Entry<Integer, Rectangle> s : slots.entrySet()) {
				if (s.getValue().contains(GameHandler.mousePos)) {
					g.setPaint(Color.RED);
					g.drawRect(s.getValue().x, s.getValue().y, s.getValue().width, s.getValue().height);
					g.setPaint(Color.BLACK);

					if (contents[s.getKey()] != null) {
						GameHandler.mouseOverItems.put(s.getValue(), contents[s.getKey()]);
					} else if (getHoverText(s.getKey()) != null)
						GameHandler.mouseOverText.put(s.getValue(), getHoverText(s.getKey()));
				}
			}
		}
	}

	public String getHoverText(int slot) {
		if (slot < size) {
			return null;
		} else if (slot < MAX_SIZE) {
			return "Locked Slot";
		} else {
			switch (slot - MAX_SIZE) {
				case 0:
					return "Helmet";
				case 1:
					return "Amulet";
				case 2:
					return "Chestpiece";
				case 3:
					return "Ring";
				case 4:
					return "Greaves";
				case 5:
					return "Ring";
				case 6:
					return "Boots";
				case 7:
					return "Anklet";
				default:
					return "Broken Slot";
			}
		}
	}

	public void onClick(Point p) {
		for (Entry<Integer, Rectangle> s : slots.entrySet()) {
			if ((s.getKey() < size || s.getKey() > MAX_SIZE) && s.getValue().contains(p)) {
				int slot = s.getKey();

				if (onCursor == null) {
					if (contents[slot] != null) {
						onCursor = contents[slot];
						onCursorSlot = slot;
						contents[slot] = null;
					}
				} else if (onCursor.type.validSlots.contains(slot)) {
					if (contents[slot] == null) {
						contents[slot] = onCursor;
						onCursor = null;
						onCursorSlot = 0;
					} else {
						Item swap = contents[slot];
						contents[slot] = onCursor;
						onCursor = swap;
					}
				}
				return;
			}
		}
		if (onCursor != null) {
			if (onCursor.type == ItemType.BLOCK && GameHandler.bounds.contains(p)) {
				TileType cursorType = TileType.fromMaterial(onCursor.mat);
				boolean intersectsSprite = false;
				for (Sprite s : GameHandler.getCurrentWorld().getSprites()) {
					if (s.getBounds().intersects(
							new Rectangle(onCursorPos.x, onCursorPos.y, cursorType.getWidth(), cursorType.getHeight()))) {
						intersectsSprite = true;
					}
				}
				if (!intersectsSprite) {
					Point place = (Point) onCursorPos.clone();
					place.translate(-GameHandler.bounds.x, -GameHandler.bounds.y);
					Class<? extends Tile> clazz = cursorType.clazz != null ? cursorType.clazz : Tile.class;
					try {
						Tile t = clazz.getConstructor(Point.class, TileType.class).newInstance(place, cursorType);
						GameHandler.getCurrentWorld().addSprite(t);
					} catch (Exception e) {
						e.printStackTrace();
					}

					onCursor = null;
					onCursorSlot = 0;
				}
			} else if (contents[onCursorSlot] == null) {
				contents[onCursorSlot] = onCursor;
				onCursor = null;
				onCursorSlot = 0;
			}
		} else {
			if (Tile.isInsideTile(p)) {
				for (Sprite s : GameHandler.getCurrentWorld().getSprites().toArray(new Sprite[0])) {
					if (s instanceof Tile && s.getBounds().contains(p)) {
						Tile t = (Tile) s;
						if (t.type.buildable) {
							GameHandler.getCurrentWorld().addSprite(
									new ItemDrop(new Point((int) t.getX(), (int) t.getY()), t.type.getItem()));
							GameHandler.getCurrentWorld().removeSprite(t);
							break;
						}
					}
				}
			}
		}
	}

	public boolean onUse(MainCharacter mc, Point p) {
		for (Entry<Integer, Rectangle> s : slots.entrySet()) {
			if (s.getKey() < size && s.getValue().contains(p)) {
				if (contents[s.getKey()] != null && contents[s.getKey()].type == ItemType.CONSUMABLE) {
					Item i = contents[s.getKey()];
					if (i.mat.onUse(mc, i))
						contents[s.getKey()] = null;
					return true;
				}
			}
		}
		return false;
	}

	public int amountOf(Item i) {
		int ret = 0;
		for (Item it : contents) {
			if (it != null) {
				if (i.similar(it)) {
					ret++;
				}
			}
		}
		return ret;
	}

	public boolean contains(Item i) {
		for (Item it : contents) {
			if (it == null)
				continue;
			if (it.similar(i)) {
				return true;
			}
		}
		return false;
	}

}
