import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public class Aldric extends NPC {
	private String[] text = new String[2];

	public Aldric(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown) throws SlickException {
		super(x, y, imgPath, name, HP, dmg, cooldown);
		text[0] = "Please seek out the Elixir of Life to cure the king.";
		text[1] = "The Elixir! My father is cured! Thank you!";
	}

	public void interactPlayer(Player player) {
		dispText = text[0];

		for (Item item : player.getItems()) {
			if (item.getId() == 3) {
				dispText = text[1];
				player.removeElixir();
			}
		}
	}

}
