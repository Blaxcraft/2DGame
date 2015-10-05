package us.mcsw.game.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import us.mcsw.game.GameHandler;
import us.mcsw.game.worlds.WorldID;

public class Sprite implements Serializable {

	private static final long serialVersionUID = 1L;

	private double		x;
	private double		y;
	protected double	realX, realY;
	public double		width;
	public double		height;
	protected boolean	vis;

	private int imageCount = -1;

	protected int	anim_rate	= 25, anim_index = 0, anim_shown = 0;
	private int		anim_buff	= 0, anim_length = 0;

	public double scale = 1;

	public static HashMap<String, Image> loadedImages = new HashMap<>();

	protected ArrayList<String> imgPaths = new ArrayList<>();

	public WorldID curWorld = null;

	public String path;

	public HashMap<Integer, Integer> colorReplace = new HashMap<>();

	public Sprite(Point p, String path) {
		init(p.x, p.y, path);
		loadImages();
	}

	public Sprite(Point p, String path, int defLength) {
		init(p.x, p.y, path);
		loadImages(defLength);
	}

	private void init(double sx, double sy, String path) {
		this.path = (path.startsWith("/") ? "" : "/") + path
				+ (path.endsWith("_%s.png") ? "" : "_%s.png");
		setX(sx);
		setY(sy);
		vis = true;
	}

	protected void getImageDimensions() {
		width = getImage().getWidth(null);
		height = getImage().getHeight(null);
	}

	protected int getImageCount() {
		if (imageCount <= -1) {
			int li = 0;
			while (getClass().getResource(String.format(path, li)) != null) {
				li++;
			}
			imageCount = li;
		}
		return imageCount;
	}

	protected void loadImages() {
		loadImages(getImageCount());
	}

	protected void loadImages(int defLength) {
		if (imgPaths == null) {
			imgPaths = new ArrayList<>();
		}
		anim_length = defLength;
		int count = getImageCount();
		for (int i = 0; i < count; i++) {
			String pth = String.format(path, i);
			imgPaths.add(pth);
		}
		getImageDimensions();
	}

	public Sprite replaceColor(Color toReplace, Color replaceWith) {
		colorReplace.put(toReplace.getRGB(), replaceWith.getRGB());
		return this;
	}

	public Sprite removeReplace(Color toReplace) {
		colorReplace.remove(toReplace.getRGB());
		return this;
	}

	public Image loadFromFile() {
		String pth = imgPaths.get(anim_shown);
		Image ret = null;
		try {
			ret = ImageIO.read(getClass().getResource(pth));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public Image getImage() {
		if (imgPaths == null || imgPaths.size() == 0) {
			loadImages(anim_length);
		}
		Image ret;
		String pth = imgPaths.get(anim_shown);
		if (colorReplace.size() > 0) {
			pth += toString();
		}
		if (!loadedImages.containsKey(pth)) {
			ret = loadFromFile();
			loadedImages.put(pth, ret);
		} else {
			ret = loadedImages.get(pth);
		}
		if (scale != 1 && width > 0 && height > 0) {
			BufferedImage resize = new BufferedImage((int) width, (int) height,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = resize.createGraphics();
			g2.drawImage(ret, 0, 0, (int) (width * scale),
					(int) (height * scale), null);
			g2.dispose();
			ret = resize;
		}

		if (colorReplace.size() > 0) {
			BufferedImage buff = toBufferedImage(ret);
			for (int x = 0; x < buff.getWidth(); x++) {
				for (int y = 0; y < buff.getHeight(); y++) {
					for (Entry<Integer, Integer> e : colorReplace.entrySet()) {
						if (e.getKey() == buff.getRGB(x, y)) {
							buff.setRGB(x, y, e.getValue());
						}
					}
				}
			}
			ret = buff;
		}

		return ret;
	}

	public void nextFrame() {
		anim_buff++;
		if (anim_buff > anim_rate) {
			anim_buff = 0;
			anim_index++;
			if (anim_index >= anim_length) {
				anim_index = 0;
			}
		}
		anim_shown = anim_index;
	}

	public BufferedImage getBufferedImage() {
		return toBufferedImage(getImage());
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
		this.realX = x + GameHandler.bounds.x;
	}

	public void setY(double y) {
		this.y = y;
		this.realY = y + GameHandler.bounds.y;
	}

	public boolean isVisible() {
		return vis;
	}

	public void setVisible(Boolean visible) {
		vis = visible;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) realX, (int) realY, (int) width,
				(int) height);
	}

	public void render(Graphics2D g) {
		render(g, getImage());
	}

	public void render(Graphics2D g, Image img) {
		g.drawImage(img, (int) realX, (int) realY, null);
	}

	public static BufferedImage toBufferedImage(Image i) {
		if (i instanceof BufferedImage) {
			return (BufferedImage) i;
		}

		BufferedImage ret = new BufferedImage(i.getWidth(null),
				i.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bgr = ret.createGraphics();
		bgr.drawImage(i, 0, 0, null);
		bgr.dispose();

		return ret;
	}

	public static BufferedImage flipImage(Image i) {
		BufferedImage img = toBufferedImage(i);
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-img.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(img, null);
	}

	public void tick() {
	}

}
