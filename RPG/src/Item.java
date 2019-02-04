import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public class Item extends Entity {
	private int id;

	public Item(float x, float y, String imgPath, String name, int id) throws SlickException {
		super(x, y, imgPath, name);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	/**
	 * Adds the item to the players inventory and buffs them.
	 * 
	 * @param player
	 */
	public void pickupItem(Player player) {
		player.buffPlayer(this);
	}

	/**
	 * Draws an item in the status panel at the bottom
	 * 
	 * @param x
	 * @param y
	 */
	public void drawInPanel(float x, float y) {
		img.draw(x, y);
	}

}
