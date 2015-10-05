package us.mcsw.game;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Random;

import us.mcsw.game.inv.CraftingManager;
import us.mcsw.game.inv.Item;
import us.mcsw.game.sprites.MainCharacter;
import us.mcsw.game.sprites.Sprite;
import us.mcsw.game.worlds.World;
import us.mcsw.game.worlds.WorldID;

public class GameHandler implements Runnable {

	public static volatile boolean				running					= false;

	public static MainCharacter					mc						= null;

	public static Rectangle						bounds					= new Rectangle(70, 25, 800, 800);

	public static long							worldSeed				= 0;

	static String								saveDir					= "worlds/", dir = saveDir + "%s.wld";

	public static WorldID						currentWorld			= null;

	public static Point							mousePos				= new Point();

	public static final Point[]					WORLD_START_POSITIONS	= { new Point(25, 25),
			new Point(bounds.width - 25 - 16, 25), new Point(25, bounds.height - 25 - 32),
			new Point(bounds.width - 25 - 16, bounds.height - 25 - 32)	};

	public static HashMap<Rectangle, String>	mouseOverText			= new HashMap<>();
	public static HashMap<Rectangle, Item>		mouseOverItems			= new HashMap<>();

	public static final int						TICKS_PER_SECOND		= 100;

	/**
	 * Tick loop
	 */
	@Override
	public void run() {
		if (loadChar() == null) {
			Point spawn = new Point(GameHandler.bounds.width / 2, GameHandler.bounds.height / 2);
			double ang = getRandom().nextDouble() * 2 * Math.PI;
			spawn.translate((int) (Math.sin(ang) * 50), (int) (Math.cos(ang) * 50));
			mc = new MainCharacter(spawn);
		} else {
			mc = GameHandler.loadChar();
		}

		if (GameHandler.mc.saved_world_at != null) {
			GameHandler.currentWorld = mc.saved_world_at;
			GameHandler.mc.saved_world_at = null;
		}

		CraftingManager.addInitalRecipes();

		Main.frame.mpanel.startRenderTicks();

		long timeOfLastTick = 0;
		long millisSinceLastTick = 0;
		while (running) {
			timeOfLastTick = System.currentTimeMillis();

			// update everything

			mc.update(millisSinceLastTick);

			for (Sprite s : GameHandler.getCurrentWorld().getSprites()) {
				s.tick();
			}

			try {
				Thread.sleep(1000 / TICKS_PER_SECOND);
			} catch (Exception e) {
				e.printStackTrace();
			}
			millisSinceLastTick = System.currentTimeMillis() - timeOfLastTick;
		}
	}

	public static void loadDefaultWorlds() {
		currentWorld = WorldID.getBaseId();
	}

	public static WorldID generateNewWorld(Class<? extends World> type, int level, int exitPortalPos) {
		try {
			World w = type.getConstructor(Integer.TYPE, Integer.TYPE).newInstance(level, exitPortalPos);
			return w.getId();
		} catch (Exception e) {
			System.out.println("ERROR while generating new world with type " + type.getClass().getSimpleName() + " : "
					+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static World getCurrentWorld() {
		return getWorld(currentWorld);
	}

	public static World getWorld(WorldID id) {
		if (id.getWorld() != null) {
			return id.getWorld();
		}

		World w = getWorldFromFile(id);
		if (w != null) {
			return w;
		}
		return null;
	}

	private static Random	rand	= null;

	public static Random getRandom() {
		if (rand == null) {
			rand = new Random();
		}
		return rand;
	}

	public static void saveChar(MainCharacter mc) {
		try {
			File saveTo = new File(saveDir + "char.plr");
			if (saveTo.exists()) {
				saveTo.delete();
			}
			saveTo.createNewFile();
			FileOutputStream fOut = new FileOutputStream(saveTo);
			ObjectOutputStream out = new ObjectOutputStream(fOut);
			out.writeObject(mc);
			out.close();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MainCharacter loadChar() {
		MainCharacter mc = null;
		try {
			FileInputStream fIn = new FileInputStream(new File("worlds/char.plr"));
			ObjectInputStream in = new ObjectInputStream(fIn);
			mc = (MainCharacter) in.readObject();
			in.close();
			fIn.close();
		} catch (FileNotFoundException e) {
			// ignore
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mc;
	}

	public static void saveWorldToFile(WorldID id) {
		try {
			File saveTo = new File(String.format(dir, id.identifier));
			if (saveTo.exists()) {
				saveTo.delete();
			}
			saveTo.createNewFile();
			FileOutputStream fOut = new FileOutputStream(saveTo);
			ObjectOutputStream out = new ObjectOutputStream(fOut);
			out.writeObject(id.getWorld());
			out.close();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO save all worlds in one file
	public static World getWorldFromFile(WorldID id) {
		try {
			World w = null;
			String pth = String.format(dir, id.identifier);
			FileInputStream fIn = new FileInputStream(new File(pth));
			ObjectInputStream in = new ObjectInputStream(fIn);
			w = (World) in.readObject();
			in.close();
			fIn.close();

			return w;
		} catch (FileNotFoundException e) {
			// ignore
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static Thread	tick	= null;

	public static void startGameTicks() {
		tick = new Thread(new GameHandler());
		tick.start();
	}

}
