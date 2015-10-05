package us.mcsw.game.inv;

import java.awt.Color;

public enum Rarity {

	Bland(
		new Color(175, 175, 175)),
	Common(
		Color.WHITE),
	Uncommon(
		Color.GREEN),
	Rare(
		Color.CYAN),
	Exotic(
		Color.MAGENTA),
	Epic(
		Color.PINK),
	Legendary(
		new Color(150, 60, 150)),
	Godly(
		Color.YELLOW),
	Relic(
		new Color(225, 150, 25)),
	Artifact(
		new Color(255, 50, 50));

	public Color	color;

	private Rarity(Color c) {
		this.color = c;
	}

	public double randomWeight() {
		return 1.0d / ((double) ordinal() + 1.0d);
	}

}
