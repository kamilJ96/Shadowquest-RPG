import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski) 
 */

public class Player extends Unit {
	private Image panel = new Image("assets/panel.png");
	private ArrayList<Item> items = new ArrayList<Item>();
	private float startX, startY;

	/**
	 * Initialises a player object with an associated image file
	 * 
	 * @param x
	 *            The player's starting x-coordinate
	 * @param y
	 *            The player's starting y-coordinate
	 * @param imgPath
	 *            Path to the player's asset file
	 */
	public Player(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown) throws SlickException {
		super(x, y, imgPath, name, HP, dmg, cooldown);
		startX = x;
		startY = y;
	}

	public int getCurrHP() {
		return currentHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void healPlayer() {
		this.currentHP = maxHP;
	}

	/**
	 * Buffs a players stats based on the item picked up.
	 * 
	 * @param item
	 */
	public void buffPlayer(Item item) {
		int id = item.getId();

		if (id == 0)
			maxHP += 80;
		if (id == 1)
			maxDmg += 30;
		if (id == 2)
			cooldown -= 300;

		items.add(item);
	}

	/**
	 * If the player dies, make them respawn in the home village at full health.
	 */
	public void respawn() {
		this.healPlayer();
		this.x = startX;
		this.y = startY;
	}

	/**
	 * Removes the elixir if the player has it and has talked to the Prince
	 */
	public void removeElixir() {
		for (Item item : items) {
			if (item.getId() == 3)
				items.remove(item);
		}
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	/**
	 * Renders the player's status panel.
	 * 
	 * @param g
	 *            The current Slick graphics context.
	 */
	public void renderPanel(Graphics g) {
		// Panel colours
		Color LABEL = new Color(0.9f, 0.9f, 0.4f); // Gold
		Color VALUE = new Color(1.0f, 1.0f, 1.0f); // White
		Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f); // Black, transp
		Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f); // Red, transp

		// Variables for layout
		String text; // Text to display
		int text_x, text_y; // Coordinates to draw text
		int bar_x, bar_y; // Coordinates to draw rectangles
		int bar_width, bar_height; // Size of rectangle to draw
		int hp_bar_width; // Size of red (HP) rectangle
		int inv_x, inv_y; // Coordinates to draw inventory item

		double health_percent; // Player's health, as a percentage
		float TOP_LEFT_PANELX = this.x - RPG.screenwidth / 2;
		float TOP_LEFT_PANELY = this.y + (RPG.screenheight / 2) - RPG.PANELHEIGHT;

		// Panel background image
		panel.draw(TOP_LEFT_PANELX, TOP_LEFT_PANELY);

		// Display the player's health
		text_x = (int) (TOP_LEFT_PANELX + 15);
		text_y = (int) (TOP_LEFT_PANELY + 25);
		g.setColor(LABEL);
		g.drawString("Health:", text_x, text_y);
		text = currentHP + "/" + maxHP;

		bar_x = (int) (TOP_LEFT_PANELX + 90);
		bar_y = (int) (TOP_LEFT_PANELY + 20);
		bar_width = 90;
		bar_height = 30;
		health_percent = (float) currentHP / maxHP;
		hp_bar_width = (int) (bar_width * health_percent);
		text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
		g.setColor(BAR_BG);
		g.fillRect(bar_x, bar_y, bar_width, bar_height);
		g.setColor(BAR);
		g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
		g.setColor(VALUE);
		g.drawString(text, text_x, text_y);

		// Display the player's damage and cooldown
		text_x = (int) (TOP_LEFT_PANELX + 200);
		g.setColor(LABEL);
		g.drawString("Damage:", text_x, text_y);
		text_x += 80;
		text = Integer.toString(maxDmg);
		g.setColor(VALUE);
		g.drawString(text, text_x, text_y);
		text_x += 40;
		g.setColor(LABEL);
		g.drawString("Rate:", text_x, text_y);
		text_x += 55;
		text = Integer.toString(cooldown);
		g.setColor(VALUE);
		g.drawString(text, text_x, text_y);

		// Display the player's inventory
		g.setColor(LABEL);
		g.drawString("Items:", (TOP_LEFT_PANELX + 420), text_y);
		bar_x = (int) (TOP_LEFT_PANELX + 490);
		bar_y = (int) (TOP_LEFT_PANELY + 10);
		bar_width = 288;
		bar_height = bar_height + 20;
		g.setColor(BAR_BG);
		g.fillRect(bar_x, bar_y, bar_width, bar_height);

		inv_x = (int) (TOP_LEFT_PANELX + 490);
		inv_y = (int) TOP_LEFT_PANELY;
		for (Item item : items) {
			item.drawInPanel(inv_x, inv_y);
			inv_x += 72;
		}
	}

}
