import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public class AggressiveMonster extends Monster {
	public AggressiveMonster(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown)
			throws SlickException {
		super(x, y, imgPath, name, HP, dmg, cooldown);
	}
}
