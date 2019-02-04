import java.util.concurrent.ThreadLocalRandom;
import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public class PassiveMonster extends Monster {
	private int timer = 0, dirX, dirY;
	private boolean isFleeing = false;
	// Implement a timer that uses delta to continuously update

	public PassiveMonster(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown)
			throws SlickException {
		super(x, y, imgPath, name, HP, dmg, cooldown);
		dirX = ThreadLocalRandom.current().nextInt(-1, 2);
		dirY = ThreadLocalRandom.current().nextInt(-1, 2);
		SPEED = 0.2f;
	}

	/**
	 * Makes a passive monster wander around the world based on random
	 * directions, or keep wandering in the current direction
	 * 
	 * @param world
	 * @param delta
	 */
	public void wander(World world, int delta) {
		timer += delta;

		if (!isFleeing) {
			if (timer > 3000) {
				dirX = ThreadLocalRandom.current().nextInt(-1, 2);
				dirY = ThreadLocalRandom.current().nextInt(-1, 2);
				timer = 0;
			}
			this.moveUnit(world, delta * dirX, delta * dirY);
		} else
			flee(world, delta);
	}

	/**
	 * If a passive monster has been attacked, it needs to run away from the
	 * player, until 5 seconds have elapsed.
	 * 
	 * @param world
	 * @param delta
	 */
	public void flee(World world, int delta) {
		if (timer > 5000) {
			isFleeing = false;
			timer = 0;
			wander(world, delta);
		} else {
			isFleeing = true;
			this.calcMove(world.getPlayer().getX(), world.getPlayer().getY(), world, delta, 1);
		}

	}

}
