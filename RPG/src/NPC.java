import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public abstract class NPC extends Unit {
	protected int timer = 0;
	protected String dispText = new String("");

	public NPC(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown) throws SlickException {
		super(x, y, imgPath, name, HP, dmg, cooldown);
	}

	/**
	 * Updates the dialogue timer according to delta
	 */
	public void update(int delta) {
		timer += delta;
	}

	/**
	 * Displays an NPCs dialogue above their head
	 * 
	 * @param g
	 *            Slick graphics object
	 */
	public void dispDialogue(Graphics g) {
		if (timer < 4000) {
			Color VALUE = new Color(1.0f, 1.0f, 1.0f); // White
			Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f); // Black, transp

			float X_TRANSLATION = this.x - img.getWidth() / 2;
			float Y_TRANSLATION = this.y - 80;

			int bar_width = g.getFont().getWidth(dispText);
			int bar_height = g.getFont().getHeight(dispText);

			g.setColor(BAR_BG);
			g.fillRect(X_TRANSLATION - bar_width / 2, Y_TRANSLATION, bar_width, bar_height);
			g.setColor(VALUE);
			g.drawString(dispText, X_TRANSLATION - bar_width / 2, Y_TRANSLATION);
		} else {
			timer = 0;
			dispText = "";
		}
	}

}
