package us.mcsw.game;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JOptionPane;

import us.mcsw.game.sprites.MainCharacter;
import us.mcsw.game.worlds.WorldID;

public class Main {

	public static Dimension	windowSize	= new Dimension(1600, 900);

	public static MainFrame	frame		= null;

	public static void main(String[] args) {

		int jop = JOptionPane.showConfirmDialog(null, "Would you like to load from file?", "Load from File",
				JOptionPane.YES_NO_OPTION);
		if (jop == -1) {
			System.exit(0);
		} else if (jop == 1) {
			File worlds = new File("worlds/");
			if (!worlds.exists()) {
				worlds.mkdir();
			}
			for (File f : worlds.listFiles()) {
				f.delete();
			}
		}

		GameHandler.loadDefaultWorlds();
		GameHandler.running = true;

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame = new MainFrame();
				frame.setVisible(true);

				GameHandler.startGameTicks();
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				GameHandler.running = false;
				GameHandler.saveWorldToFile(WorldID.getBaseId());
				GameHandler.saveWorldToFile(GameHandler.getCurrentWorld().getId());
				MainCharacter mc = GameHandler.mc;
				mc.saved_world_at = GameHandler.getCurrentWorld().getId();
				GameHandler.saveChar(mc);
			}
		}));
	}

}
