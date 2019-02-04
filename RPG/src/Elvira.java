import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public class Elvira extends NPC {
	private String[] text = new String[2];

	public Elvira(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown) throws SlickException {
		super(x, y, imgPath, name, HP, dmg, cooldown);
		text[0] = "Return to me if you ever need healing.";
		text[1] = "You're looking much healthier now";
	}

	/**
	 * Heals the player if they have taken damage
	 * 
	 * @param player
	 */
	public void interactPlayer(Player player) {
		// This 'if' statement makes it so the player doesn't talk too many
		// times at once, thus changing the dialogue too quickly
		if (timer > 500) {
			if (player.getCurrHP() < player.getMaxHP()) {
				player.healPlayer();
				dispText = text[1];
				timer = 0;
			} else
				dispText = text[0];
		}
	}

}
