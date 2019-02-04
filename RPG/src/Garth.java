import org.newdawn.slick.SlickException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

public class Garth extends NPC {
	private String[] text = new String[4];

	public Garth(float x, float y, String imgPath, String name, int HP, int dmg, int cooldown) throws SlickException {
		super(x, y, imgPath, name, HP, dmg, cooldown);
		text[0] = "Find the Amulet of Vitality, across the river to the west.";
		text[1] = "Find the Sword of Strength - cross the bridge to the east, then head south.";
		text[2] = "Find the Tome of Agility, in the Land of Shadows";
		text[3] = "You have found all the treasure I know of";
	}

	/**
	 * Chooses the text to display based on what items the player has
	 * 
	 * @param player
	 */
	public void interactPlayer(Player player) {
		int lowestId = 0;
		for (Item item : player.getItems()) {
			int itemId = item.getId();
			if (itemId <= lowestId)
				lowestId++;
		}

		dispText = text[lowestId];
	}

}
