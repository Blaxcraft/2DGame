package us.mcsw.game.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;

import us.mcsw.game.GameHandler;
import us.mcsw.game.inv.Inventory;

public enum Stats {

	Attack(
		10,
		new Color(100, 255, 100)),
	Defense(
		0,
		new Color(100, 100, 255)),
	Health(
		100,
		new Color(255, 100, 100)),
	Crafting(
		0,
		new Color(50, 25, 0)),
	Speed(
		100,
		new Color(100, 255, 255));

	private static HashMap<Stats, Integer>	stats	= new HashMap<>();
	private static HashMap<Stats, Integer>	boosts	= new HashMap<>();

	int										def;
	Color									color;

	Stats(int def, Color color) {
		this.def = def;
		this.color = color;
	}

	public int get() {
		if (!stats.containsKey(this)) {
			stats.put(this, def);
		}
		int ret = stats.get(this);
		if (boosts.containsKey(this)) {
			ret += getBoost();
		}
		return ret;
	}

	public void set(int set) {
		stats.remove(this);
		stats.put(this, set);
	}

	public void add(int add) {
		set(get() + add);
	}

	public int getBoost() {
		if (!boosts.containsKey(this)) {
			boosts.put(this, 0);
		}
		return boosts.get(this);
	}

	public void setBoost(int set) {
		boosts.remove(this);
		boosts.put(this, set);
	}

	public void addBoost(int add) {
		setBoost(getBoost() + add);
	}

	public static void damagePlayer(int dmg, DamageCause cause) {
		if (cause != DamageCause.MAGIC) {
			dmg -= Defense.get() / 2;
			if (dmg < 1) {
				dmg = 1;
			}
		}
		Health.addBoost(-dmg);
		if (Health.getBoost() >= Health.get()) {
			// TODO Death
		}
	}

	public static void healPlayer(int health, HealCause cause) {
		Health.addBoost(health);
		if (Health.getBoost() > 0) {
			Health.setBoost(0);
		}
	}

	public static void calculateBoosts(Inventory inv) {
		for (Stats s : Stats.values()) {
			if (s != Health)
				s.setBoost(0);
		}

		for (int i = Inventory.MAX_SIZE; i < Inventory.MAX_SIZE + Inventory.EQUIPMENT_SLOTS; i++) {
			if (inv.contents[i] != null) {
				for (String s : inv.contents[i].details) {
					if (s.startsWith("+")) {
						for (Stats st : values()) {
							if (s.contains(st.name())) {
								int boost = Integer.parseInt(s.substring(1).replace(" " + st.name(), ""));
								st.addBoost(boost);
							}
						}
					}
				}
			}
		}
	}

	public static void render(Graphics2D g2) {
		Font defFont = g2.getFont();

		int stX = GameHandler.bounds.x + GameHandler.bounds.width + 20, stY = GameHandler.bounds.y;
		int sizeX = 80, sizeY = 40;
		for (Stats s : values()) {
			g2.setPaint(new Color(200, 175, 150));
			g2.fillRect(stX, stY, sizeX, sizeY);
			g2.setPaint(s.color);
			for (int i = 0; i < 3; i++) {
				g2.drawRect(stX - i, stY - i, sizeX + (2 * i), sizeY + (2 * i));
			}
			g2.setFont(new Font(g2.getFont().getFontName(), Font.BOLD, 25));
			String draw = s.get() + "";
			g2.drawString(draw, stX + ((sizeX / 2) - (g2.getFontMetrics().stringWidth(draw) / 2)), stY + (sizeY / 2)
					+ 10);

			g2.setFont(new Font(g2.getFont().getFontName(), Font.PLAIN, 18));
			g2.drawString(s.name(), stX + ((sizeX / 2) - (g2.getFontMetrics().stringWidth(s.name()) / 2)), stY + sizeY
					+ 20);

			stX += sizeX + 30;
		}

		g2.setFont(defFont);
	}

}
