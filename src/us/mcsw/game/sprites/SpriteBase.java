package us.mcsw.game.sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import us.mcsw.game.GameHandler;
import us.mcsw.game.inv.CraftingManager;
import us.mcsw.game.inv.CraftingManager.Recipe;
import us.mcsw.game.inv.Inventory;
import us.mcsw.game.inv.Item;

public class SpriteBase extends Sprite {

	private static final long	serialVersionUID	= 1L;

	public Color[]				colors				= new Color[3];

	public SpriteBase() {
		super(new Point(GameHandler.bounds.width / 2 - 8, GameHandler.bounds.height / 2 - 8), "images/tiles/base");
		for (int i = 0; i < colors.length; i++) {
			colors[i] = new Color(0, 0, 0);
		}
	}

	Random		rand			= new Random();

	public int	cooldown		= 0;
	public int	cooldownTime	= 20;

	@Override
	public Image getImage() {
		Image ii = super.getImage();
		BufferedImage buff = toBufferedImage(ii);
		if (colors != null) {
			if (--cooldown < 0) {
				colors[0] = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
				colors[1] = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
				colors[2] = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
				cooldown = cooldownTime;
			}

			for (int x = 0; x < buff.getWidth(); x++) {
				for (int y = 0; y < buff.getHeight(); y++) {
					Color c = new Color(buff.getRGB(x, y));
					if (c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 0) {
						buff.setRGB(x, y, colors[0].getRGB());
					} else if (c.getRed() == 0 && c.getGreen() == 255 && c.getBlue() == 0) {
						buff.setRGB(x, y, colors[1].getRGB());
					} else if (c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 255) {
						buff.setRGB(x, y, colors[2].getRGB());
					}
				}
			}
		}

		return buff;
	}

	@Override
	public void render(Graphics2D g, Image img) {
		GameHandler.mouseOverText.put(getBounds(), "Base Block (Crafting)");
		super.render(g, img);
	}

	public static HashMap<Rectangle, Recipe>	craft			= new HashMap<>();

	static int									scrollX			= 0;

	static int									prevMaxScroll	= 0;

	static Rectangle							bounds			= new Rectangle(GameHandler.bounds.x
																		+ GameHandler.bounds.width + 20, 500, 650, 500);

	public static void renderCraftingMenu(Graphics2D g, Inventory inv) {
		g.setPaint(new Color(100, 100, 100));
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		ArrayList<Recipe> current = new ArrayList<>();
		for (int i = 0; i < Inventory.MAX_SIZE; i++) {
			Item it = inv.contents[i];
			if (it != null) {
				for (Recipe r : CraftingManager.getRecipesFor(it)) {
					if (!current.contains(r)) {
						current.add(r);
					}
				}
			}
		}

		craft.clear();

		int invX = 50;
		int invY = 50;

		prevMaxScroll = (current.size() - 5) * 60;
		if (prevMaxScroll < 0) {
			prevMaxScroll = 0;
		}

		if (scrollX > prevMaxScroll) {
			scrollX = prevMaxScroll;
		}

		int sy = bounds.y + 60 - scrollX;
		for (Recipe r : current) {
			int tx = bounds.x + 20;
			if (sy > bounds.y) {
				g.setPaint(new Color(200, 200, 200));
				g.fillRect(tx, sy, invX, invY);
				g.setPaint(new Color(r.canCraft(inv) ? 100 : 255, r.canCraft(inv) ? 255 : 100, 100));
				g.drawRect(tx, sy, invX, invY);
				g.drawImage(r.product.getImage(), tx + (invX / 2) - (r.product.getImage().getWidth(null) / 2), sy
						+ (invY / 2) - (r.product.getImage().getHeight(null) / 2), null);

				GameHandler.mouseOverItems.put(new Rectangle(tx, sy, invX, invY), r.product);

				tx += invX + 30;

				HashMap<Item, Integer> counted = new HashMap<>();

				for (Item i : r.ingredients) {
					if (!counted.containsKey(i))
						counted.put(i, inv.amountOf(i));
					boolean enough = counted.get(i) > 0;

					int am = counted.get(i);
					counted.remove(i);
					counted.put(i, am - 1);

					g.setPaint(new Color(200, 200, 200));
					g.fillRect(tx, sy, invX, invY);
					g.setPaint(new Color(enough ? 100 : 255, enough ? 255 : 100, 100));
					g.drawRect(tx, sy, invX, invY);
					g.drawImage(i.getImage(), tx + (invX / 2) - (i.getImage().getWidth(null) / 2), sy + (invY / 2)
							- (i.getImage().getHeight(null) / 2), null);

					GameHandler.mouseOverItems.put(new Rectangle(tx, sy, invX, invY), i);

					tx += invX + 10;
				}

				craft.put(new Rectangle(bounds.x + 20, sy, invX, invY), r);
			}

			sy += invY + 10;
		}

		g.setPaint(new Color(150, 150, 150));
		g.fillRect(bounds.x, bounds.y, bounds.width, 50);

		g.setPaint(new Color(200, 200, 200));
		g.drawRect(bounds.x, bounds.y, bounds.width - 1, 50);

		g.setPaint(Color.black);
		g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 36));
		String title = "CRAFTING <WIP>";
		g.drawString(title, bounds.x + (bounds.width / 2) - (g.getFontMetrics().stringWidth(title) / 2), bounds.y + 40);
	}

	public static void onClick(Point p, Inventory inv) {
		for (Entry<Rectangle, Recipe> e : craft.entrySet()) {
			if (e.getKey().contains(p)) {
				if (Inventory.onCursor == null && e.getValue().canCraft(inv)) {
					for (Item in : e.getValue().ingredients) {
						inv.removeItem(in);
					}
					Inventory.onCursor = e.getValue().product;
				}
			}
		}
	}

	public static void mouseWheelMoved(MouseWheelEvent e) {
		int dir = e.getWheelRotation();
		scrollX += dir * 50;
		if (scrollX < 0) {
			scrollX = 0;
		}
		if (scrollX > prevMaxScroll) {
			scrollX = prevMaxScroll;
		}
	}
}
