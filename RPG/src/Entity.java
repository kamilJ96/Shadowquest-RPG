import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public abstract class Entity {
	protected float x, y;
	protected Image img, imgR, imgL;
	protected String name;
	protected boolean flipped = false, isDrawn = true;

	public Entity(float x, float y, String imgpath, String name) throws SlickException {
		this.x = x;
		this.y = y;

		imgR = new Image(imgpath);
		imgL = imgR.getFlippedCopy(true, false);

		this.name = name;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	/**
	 * Draws an entity onto the screen
	 * 
	 * @param x
	 *            The x position to draw the entity
	 * @param y
	 *            The y position to draw the entity
	 */
	public void draw() {
		if (isDrawn) {
			if (!flipped)
				img = imgR;
			else
				img = imgL;
			img.drawCentered((int) x, (int) y);
		}
	}
}
