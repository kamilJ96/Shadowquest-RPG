import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public abstract class Monster extends Unit {

	public Monster(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown) throws SlickException {
		super(x, y, imgPath, name, HP, dmg, cooldown);
	}

	/**
	 * Calculates where a monster should move based on the players coordinates
	 * 
	 * @param playerX
	 * @param playerY
	 * @param world
	 * @param delta
	 * @param dir
	 *            The direction to go (-1 = go towards player, 1 = go away from
	 *            player)
	 */
	public void calcMove(float playerX, float playerY, World world, int delta, int dir) {
		float distX = this.x - playerX;
		float distY = this.y - playerY;

		float distTotal = (float) Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));

		this.moveUnit(world, dir * (distX / distTotal) * delta, dir * (distY / distTotal) * delta);
	}

	/**
	 * Overloaded method for AggressiveMonsters to chase the player
	 * 
	 * @param playerX
	 * @param playerY
	 * @param world
	 * @param delta
	 */
	public void calcMove(float playerX, float playerY, World world, int delta) {
		this.calcMove(playerX, playerY, world, delta, -1);
	}
}
