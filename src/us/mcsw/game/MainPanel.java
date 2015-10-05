package us.mcsw.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import us.mcsw.game.inv.Item;
import us.mcsw.game.player.Stats;
import us.mcsw.game.sprites.Sprite;

public class MainPanel extends JPanel implements Runnable {

	private static final long	serialVersionUID	= 1L;

	private Thread				render;

	private static int			framesLastSecond	= 0, fps = 0;

	public MainPanel() {
		initializePanel();
	}

	private void initializePanel() {
		addKeyListener(new KAdapter());
		setFocusable(true);
		setDoubleBuffered(true);

		try {
			background = ImageIO.read(getClass().getResource("/images/backgrounds/background_menu.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		TimerTask updateFPS = new TimerTask() {
			public void run() {
				fps = framesLastSecond;
				framesLastSecond = 0;
			}
		};
		Timer t = new Timer();
		t.scheduleAtFixedRate(updateFPS, 0, 1000);
	}

	public void startRenderTicks() {
		render = new Thread(this);
		render.start();
	}

	BufferedImage	background	= null, worldBackground = null;

	@Override
	protected void paintComponent(Graphics g1) {
		super.paintComponent(g1);

		Point mouse = MouseInfo.getPointerInfo().getLocation();
		Point locOnScreen = getLocationOnScreen();
		mouse.translate(-locOnScreen.x, -locOnScreen.y);
		GameHandler.mousePos = mouse;

		Graphics2D g2 = (Graphics2D) g1;

		g2.setPaint(new TexturePaint(background, new Rectangle(16, 16)));
		g2.fillRect(0, 0, getWidth(), getHeight());

		Rectangle bounds = GameHandler.bounds;
		g2.setPaint(Color.BLACK);

		g2.drawString("FPS: " + fps, 5, 15);

		Font def = g2.getFont();
		g2.setFont(new Font(g2.getFont().getFontName(), Font.BOLD, 25));
		g2.drawString("World: " + GameHandler.getCurrentWorld().name + "      ID: "
				+ GameHandler.getCurrentWorld().getId().identifier, GameHandler.bounds.x + 100, GameHandler.bounds.y
				+ GameHandler.bounds.height + 30);
		g2.setFont(def);

		Stats.render(g2);

		try {
			worldBackground = ImageIO.read(getClass().getResource(GameHandler.getCurrentWorld().backgroundPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		g2.setPaint(new TexturePaint(worldBackground, new Rectangle(16, 16)));
		g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		g2.setPaint(Color.BLACK);

		GameHandler.getCurrentWorld().renderWorld(g2);

		if (GameHandler.mc != null && GameHandler.mc.isVisible()) {
			BufferedImage img = GameHandler.mc.getBufferedImage();
			if (GameHandler.mc.flip) {
				img = Sprite.flipImage(img);
			}

			GameHandler.mc.render(g2, img);
		}

		g2.setFont(def);

		if (GameHandler.mc != null)
			GameHandler.mc.inv.render(g2);

		for (Entry<Rectangle, String> e : GameHandler.mouseOverText.entrySet()) {
			if (e.getKey().contains(mouse)) {
				g2.drawString(e.getValue(), mouse.x + 5, mouse.y + 5);
			}
		}
		GameHandler.mouseOverText.clear();

		for (Entry<Rectangle, Item> e : GameHandler.mouseOverItems.entrySet()) {
			if (e.getKey().contains(mouse)) {
				e.getValue().drawMouseOver(g2, mouse);
			}
		}
		GameHandler.mouseOverItems.clear();

		Toolkit.getDefaultToolkit().sync();
	}

	Random	rand	= new Random();

	@Override
	public void run() {
		long befTime = System.currentTimeMillis(), timeDif = 0, sleep = 0;

		while (GameHandler.running) {
			repaint();

			timeDif = System.currentTimeMillis() - befTime;
			sleep = 10 - timeDif;

			if (sleep < 0) {
				sleep = 2;
			}

			try {
				Thread.sleep(sleep);
			} catch (Exception e) {
				e.printStackTrace();
			}

			framesLastSecond++;

			befTime = System.currentTimeMillis();
		}
	}

	private class KAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			GameHandler.mc.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			GameHandler.mc.keyPressed(e);
		}
	}

}
