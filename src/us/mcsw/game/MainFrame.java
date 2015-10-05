package us.mcsw.game;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;

import us.mcsw.game.inv.Inventory;
import us.mcsw.game.sprites.SpriteBase;

public class MainFrame extends JFrame implements MouseListener, MouseWheelListener {

	private static final long	serialVersionUID	= 1L;

	public MainFrame() {
		initializeUI();
	}

	public MainPanel	mpanel;

	private void initializeUI() {
		MainPanel panel = new MainPanel();
		this.mpanel = panel;
		add(panel);

		setSize(Main.windowSize);

		addMouseListener(this);
		addMouseWheelListener(this);

		setTitle("2D Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setResizable(false);
		setPreferredSize(Main.windowSize);
		pack();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) {
			Point p = e.getPoint();
			p.translate(-3, -GameHandler.bounds.y); // why
			Inventory inv = GameHandler.mc.inv;
			inv.onClick(p);
			SpriteBase.onClick(p, inv);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		SpriteBase.mouseWheelMoved(e);
	}

}
