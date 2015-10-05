package us.mcsw.game.creatures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum CreatureType {
	
	Spider("spider");
	
	public String path;
	
	private CreatureType(String path) {
		this.path = path;
		
		loadImage();
	}

	private transient BufferedImage	image;

	private void loadImage() {
		try {
			image = ImageIO.read(new File(getFullPath() + "_0.png"));
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
	
}
