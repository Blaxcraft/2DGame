package us.mcsw.game.inv;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.util.ArrayList;

import us.mcsw.game.sprites.Sprite;

public class Item extends Sprite implements Cloneable {

	private static final long	serialVersionUID	= 1L;

	public String				displayName;
	public ArrayList<String>	details;
	public ItemType				type;
	public Material				mat;
	public Rarity				rarity;

	public Item(Material mat, ItemType type, String name, Rarity rarity, String... details) {
		super(new Point(0, 0), mat.getFullPath());
		init(mat, type, name, rarity, null, details);
	}

	public Item(Material mat, ItemType type, String name, Rarity rarity, Color replace, String... details) {
		super(new Point(0, 0), mat.getFullPath());
		init(mat, type, name, rarity, replace, details);
	}

	private void init(Material mat, ItemType type, String name, Rarity rarity, Color replace, String... details) {
		displayName = name;
		this.type = type;
		ArrayList<String> det = new ArrayList<String>();
		if (type != null && type != ItemType.DEFAULT) {
			det.add(type.name().substring(0, 1) + type.name().substring(1).toLowerCase());
		}
		for (String s : details) {
			det.add(s);
		}
		this.details = det;
		this.mat = mat;
		this.rarity = rarity;
		if (replace != null && mat.replace != null)
			replaceColor(mat.replace, replace);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean similar(Item it) {
		if (it.displayName.equalsIgnoreCase(displayName) && it.path.equals(path) && it.rarity == rarity
				&& it.details.size() == details.size()) {
			for (int i = 0; i < details.size(); i++) {
				if (!details.get(i).equals(it.details.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void drawMouseOver(Graphics2D g, Point mouse) {
		int sx = mouse.x + 10;
		int sy = mouse.y + 5;

		ArrayList<String> print = new ArrayList<>(details);
		if (rarity != null) {
			print.add(0, rarity.name());
		}
		if (type == ItemType.CONSUMABLE) {
			print.add("Press E to use");
		}

		FontMetrics fm = g.getFontMetrics();

		int mx = fm.stringWidth(displayName);
		for (String d : print) {
			int size = fm.stringWidth(d) + 15;
			if (size > mx)
				mx = size;
		}

		g.setPaint(new Color(50, 50, 50));
		g.fillRect(sx - 5, sy - 15, mx + 10, print.size() * 15 + 20);
		g.setPaint(new Color(200, 200, 200));
		g.drawRect(sx - 5, sy - 15, mx + 10, print.size() * 15 + 20);

		g.drawString(displayName, sx, sy);
		if (print.size() > 0) {
			for (int i = 0; i < print.size(); i++) {
				Paint p = g.getPaint();
				if (i == 0 && rarity != null) {
					g.setPaint(rarity.color);
				}
				g.drawString(print.get(i), sx + 15, sy + ((i + 1) * 15));
				g.setPaint(p);
			}
		}
	}

}
