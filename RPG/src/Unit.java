import java.util.concurrent.ThreadLocalRandom;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public abstract class Unit extends Entity {
	protected int maxHP, currentHP, maxDmg, cooldown, attackTimer;
	protected float SPEED = 0.25f;

	public Unit(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown) throws SlickException {
		super(x, y, imgPath, name);

		this.maxHP = this.currentHP = HP;
		this.maxDmg = dmg;
		this.cooldown = cooldown;
	}

	/**
	 * Lowers the amount of time until the unit can attack again
	 * 
	 * @param delta
	 */
	public void update(int delta) {
		attackTimer -= delta;
	}

	/**
	 * Moves the unit in the world, given that the terrain isn't blocked.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 */
	public void moveUnit(World world, double x, double y) {
		// Change direction the player is facing when moving horizontally
		if (x < 0)
			flipped = true;
		else if (x > 0)
			flipped = false;

		// Move the player 0.25 pixels for each millisecond that's passed
		double new_x = this.x + SPEED * x;
		double new_y = this.y + SPEED * y;

		// Check if the tile at the new coordinates blocks the player or not
		// If it doesn't, move the player to the new coordinates
		if (world.checkBlocked(new_x, this.y) == "0")
			this.x = (float) new_x;

		if (world.checkBlocked(this.x, new_y) == "0")
			this.y = (float) new_y;
	}

	/**
	 * Draws the Unit along with a small status bar above their image,
	 * indicating their name and current health.
	 * 
	 * @param g
	 */
	public void drawWithPanel(Graphics g) {
		if (!flipped)
			img = imgR;
		else
			img = imgL;
		img.drawCentered((int) x, (int) y);

		Color VALUE = new Color(1.0f, 1.0f, 1.0f); // White
		Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f); // Red, transp
		Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f); // Black, transp

		float X_TRANSLATION = this.x - img.getWidth() / 2;
		float Y_TRANSLATION = this.y - 60;

		int bar_x = (int) (X_TRANSLATION);
		int bar_y = (int) (Y_TRANSLATION);

		int bar_width;

		if (g.getFont().getWidth(name) > 70)
			bar_width = g.getFont().getWidth(name);
		else
			bar_width = 70;
		int bar_height = 20;

		float health_percent = (float) currentHP / maxHP;
		int hp_bar_width = (int) (bar_width * health_percent);
		int text_x = bar_x + (bar_width - g.getFont().getWidth(name)) / 2;

		g.setColor(BAR_BG);
		g.fillRect(bar_x, bar_y, bar_width, bar_height);
		g.setColor(BAR);
		g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
		g.setColor(VALUE);
		g.drawString(name, text_x, Y_TRANSLATION);

	}

	/**
	 * Rolls a random integer between 0 and max damage to determine how much
	 * damage to inflict, given that they aren't in cooldown.
	 * 
	 * @return
	 */
	public int genAttack() {
		int attack = 0;

		if (attackTimer <= 0) {
			attack = ThreadLocalRandom.current().nextInt(0, maxDmg + 1);
			attackTimer = cooldown;
		}

		return attack;
	}

	/**
	 * Inflicts damage on unit and checks to see if they died.
	 * 
	 * @param dmg
	 * @return True if dead, false if not
	 */
	public boolean getHitDead(int dmg) {
		this.currentHP -= dmg;

		if (currentHP <= 0)
			return true;

		return false;
	}

}
