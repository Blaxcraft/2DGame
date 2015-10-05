package us.mcsw.game.tiles;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import us.mcsw.game.inv.Item;
import us.mcsw.game.inv.ItemType;
import us.mcsw.game.inv.Material;
import us.mcsw.game.inv.Rarity;

public enum TileType {

	WOOD(
		"Wood",
		Rarity.Bland,
		"wood",
		0.3f,
		true,
		false,
		"It's made of trees!"),
	DOOR(
		TileDoor.class,
		"Wood Door",
		Rarity.Common,
		"door",
		0.2f,
		true,
		true,
		"It's a door. Walk through it."),
	TRAPDOOR(
		"Trapdoor",
		Rarity.Common,
		"trapdoor",
		0.2f,
		true,
		true,
		"It's a door, but horizontal"),
	LEAVES(
		"Leaves",
		Rarity.Bland,
		"leaves",
		0.1f,
		false,
		false,
		"Trees are made of them!"),
	ICE(
		"Ice",
		Rarity.Bland,
		"ice",
		0.2f,
		false,
		false,
		"Frozen solid!"),
	FROZEN_LAKE(
		"Frozen Lake",
		Rarity.Bland,
		"frozenlake",
		0.1f,
		false,
		true,
		"It's like ice, but not."),
	LOOT_CHEST(
		"Chest",
		null,
		"chest",
		0.5f,
		false,
		true,
		"Contains loot");

	public String					path;
	public float					hardness;
	public boolean					passThrough, buildable;
	public String					displayName;
	private Rarity					rarity;
	public String[]					desc;

	public Class<? extends Tile>	clazz;

	private TileType(String displayName, Rarity r, String path, float hardness, boolean buildable, boolean passThrough,
			String... desc) {
		init(displayName, r, path, hardness, buildable, passThrough, desc);
	}

	private TileType(Class<? extends Tile> clazz, String displayName, Rarity r, String path, float hardness,
			boolean buildable, boolean passThrough, String... desc) {
		init(displayName, r, path, hardness, buildable, passThrough, desc);
		this.clazz = clazz;
	}

	private void init(String displayName, Rarity r, String path, float hardness, boolean buildable,
			boolean passThrough, String... desc) {
		this.displayName = displayName;
		this.path = path;
		this.hardness = hardness;
		this.passThrough = passThrough;
		this.buildable = buildable;
		this.rarity = r;
		this.desc = desc;
		loadImage();
	}

	private transient BufferedImage	image;

	private void loadImage() {
		try {
			image = ImageIO.read(getClass().getResource("/" + getFullPath() + "_0.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public String getFullPath() {
		return "images/tiles/" + path;
	}

	public Item getItem() {
		return new Item(new Material("tiles/" + path), ItemType.BLOCK, displayName, getRarity(), desc);
	}

	public Rarity getRarity() {
		return rarity;
	}

	public static TileType fromMaterial(Material mat) {
		for (TileType t : values()) {
			if (t.getFullPath().equals(mat.getFullPath())) {
				return t;
			}
		}
		return null;
	}

}
