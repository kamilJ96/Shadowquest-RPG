
/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Represents the entire game world. (Designed to be instantiated just once for
 * the whole game).
 */
public class World {
	private TiledMap map;
	private Player player;
	private Camera camera;

	// Arrays for Items, Monsters, and NPCs in the world
	private ArrayList<Item> itemsWorld = new ArrayList<Item>();
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<NPC> npcs = new ArrayList<NPC>();

	// Dimensions of a tile, in pixels
	private final int TILE_DIM = 72;

	// Number of tiles to be rendered horizontally and vertically
	private final int TILES_DOWN = 10;
	private final int TILES_ACROSS = 13;

	// Starting coordinates for player, in pixels.
	private final int START_X = 756;
	private final int START_Y = 684;

	private final String UNITS_PATH = "assets/units";
	private final String ITEMS_PATH = "assets/items";

	/**
	 * Create a new World object.
	 * 
	 * @throws IOException
	 */
	public World() throws SlickException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader("data/units.dat"));
		String line;

		map = new TiledMap("assets/map.tmx", "assets/");
		player = new Player(START_X, START_Y, UNITS_PATH + "/player.png", "Player", 100, 26, 600);
		camera = new Camera(player, RPG.screenwidth, RPG.screenheight);

		itemsWorld.add(new Item(965, 3563, ITEMS_PATH + "/amulet.png", "Amulet of Vitality", 0));
		itemsWorld.add(new Item(546, 6707, ITEMS_PATH + "/sword.png", "Sword of Strength", 1));
		itemsWorld.add(new Item(4791, 1253, ITEMS_PATH + "/tome.png", "Tome of Agility", 2));
		itemsWorld.add(new Item(1976, 402, ITEMS_PATH + "/elixir.png", "Elixir of Life", 3));

		// Add all the units from units.dat into the world
		while ((line = reader.readLine()) != null) {
			StringTokenizer tokens = new StringTokenizer(line, ",");
			String name = tokens.nextToken();
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());

			if (name.equalsIgnoreCase("princealdric")) {
				Aldric aldric = new Aldric(x, y, UNITS_PATH + "/prince.png", "Prince Aldric", 1, 0, 0);
				npcs.add(aldric);
			}
			if (name.equalsIgnoreCase("elvira")) {
				Elvira elvira = new Elvira(x, y, UNITS_PATH + "/shaman.png", "Elvira", 1, 0, 0);
				npcs.add(elvira);
			}
			if (name.equalsIgnoreCase("garth")) {
				Garth garth = new Garth(x, y, UNITS_PATH + "/peasant.png", "Garth", 1, 0, 0);
				npcs.add(garth);
			}
			if (name.equalsIgnoreCase("draelic")) {
				AggressiveMonster draelic = new AggressiveMonster(x, y, UNITS_PATH + "/necromancer.png", "Draelic", 140,
						30, 400);
				monsters.add(draelic);
			}
			if (name.equalsIgnoreCase("giantbat")) {
				PassiveMonster new_bat = new PassiveMonster(x, y, UNITS_PATH + "/dreadbat.png", "Giant Bat", 40, 0, 0);
				monsters.add(new_bat);
			}
			if (name.equalsIgnoreCase("zombie")) {
				AggressiveMonster new_zomb = new AggressiveMonster(x, y, UNITS_PATH + "/zombie.png", "Zombie", 60, 10,
						800);
				monsters.add(new_zomb);
			}
			if (name.equalsIgnoreCase("bandit")) {
				AggressiveMonster new_band = new AggressiveMonster(x, y, UNITS_PATH + "/bandit.png", "Bandit", 40, 8,
						200);
				monsters.add(new_band);
			}
			if (name.equalsIgnoreCase("skeleton")) {
				AggressiveMonster new_skele = new AggressiveMonster(x, y, UNITS_PATH + "/skeleton.png", "Skeleton", 100,
						16, 500);
				monsters.add(new_skele);
			}
		}
		reader.close();
	}

	/**
	 * Update the game state for a frame.
	 * 
	 * @param player
	 *            The player to move
	 * @param camera
	 *            The camera object to update
	 * @param dir_x
	 *            The player's movement in the x axis (-1, 0 or 1).
	 * @param dir_y
	 *            The player's movement in the y axis (-1, 0 or 1).
	 * @param delta
	 *            Time passed since last frame (milliseconds).
	 */
	public void update(int dir_x, int dir_y, int delta, String key) throws SlickException {
		player.moveUnit(this, dir_x * delta, dir_y * delta);
		player.update(delta);

		// Make passive monsters wander around
		for (Monster monster : monsters)
			if (monster instanceof PassiveMonster) {
				PassiveMonster bat = (PassiveMonster) monster;
				bat.wander(this, delta);
			}

		// Update NPC timers
		for (NPC npc : npcs)
			npc.update(delta);

		// Pickup any items near the player
		ArrayList<Item> itemsNear = getItemsNear();
		for (Item item : itemsNear) {
			item.pickupItem(player);
			itemsWorld.remove(item);
		}

		// Get all monsters within attack range (50 pixels)
		ArrayList<Monster> monstersNear = getMonstersNear(0, 50);
		int attack;
		boolean isDead;

		// Make aggro monsters attack the player, and the player attack monsters
		for (Monster monster : monstersNear) {
			if (monster instanceof AggressiveMonster) {
				attack = monster.genAttack();
				isDead = player.getHitDead(attack);
				monster.update(delta);

				if (isDead)
					player.respawn();
			}

			if (key == "A") {
				attack = player.genAttack();
				isDead = monster.getHitDead(attack);

				if (monster instanceof PassiveMonster)
					((PassiveMonster) monster).flee(this, delta);

				if (isDead)
					monsters.remove(monster);
			}
		}

		// For any aggro monsters near the player, make them chase him
		monstersNear = getMonstersNear(51, 150);
		for (Monster monster : monstersNear) {
			if (monster instanceof AggressiveMonster) {
				monster.calcMove(player.getX(), player.getY(), this, delta);
			}
		}

		// Interact with any nearby NPCs if player pressed 'T'
		ArrayList<NPC> npcsNear = getNPCsNear();
		for (NPC npc : npcsNear) {
			if (key == "T") {
				if (npc instanceof Aldric) {
					((Aldric) npc).interactPlayer(player);
				}
				if (npc instanceof Elvira) {
					((Elvira) npc).interactPlayer(player);
				}
				if (npc instanceof Garth) {
					((Garth) npc).interactPlayer(player);
				}
			}
		}

		camera.update();
	}

	/**
	 * Checks if a new position for the player is blocked by the terrain
	 *
	 * @param x
	 *            The x value to move the player
	 * @param y
	 *            The y value to move the player
	 * @return Returns whether the terrain should block the player ("1") or not
	 *         ("0")
	 */
	public String checkBlocked(double x, double y) {
		/*
		 * The (x, y) coordinates are divided by TILE_DIM to get the actual tile
		 * that the player would be moving to (since the player is able to move
		 * within tiles)
		 */
		int tileID = map.getTileId((int) x / TILE_DIM, (int) y / TILE_DIM, 0);
		String isBlocked = map.getTileProperty(tileID, "block", "0");

		return isBlocked;
	}

	/**
	 * Render the entire screen, so it reflects the current game state. If a
	 * monster dies, or an item is picked up, it is removed from the array and
	 * hence won't be redrawn.
	 * 
	 * @param camera
	 *            The camera object to base the location in the world off of
	 */
	public void render(Graphics g) throws SlickException {
		/*
		 * Offset the rendering by up to TILE_DIM pixels. As the player moves,
		 * the offset values decrease until a new tile is reached, whereby the
		 * offset is then automatically reset
		 */
		int offsetX = 0 - camera.getxPos() % TILE_DIM;
		int offsetY = 0 - camera.getyPos() % TILE_DIM;

		/*
		 * Since the camera is positioned in the centre of the screen, half the
		 * screenwidth/height is subtracted so as to give the coordinates for
		 * the top-left pixel (where the rendering should start). This value is
		 * then divided by TILE_DIM to give the tile that should go in the
		 * top-left corner
		 */
		int tileX = camera.getxPos() / TILE_DIM;
		int tileY = camera.getyPos() / TILE_DIM;

		map.render(offsetX, offsetY, tileX, tileY, TILES_ACROSS, TILES_DOWN);

		g.translate(-camera.getMinX(), -camera.getMinY());

		player.draw();

		for (Item item : itemsWorld) {
			item.draw();
		}

		for (Monster monster : monsters) {
			monster.drawWithPanel(g);
		}

		for (NPC npc : npcs) {
			npc.drawWithPanel(g);
			npc.dispDialogue(g);
		}

		player.renderPanel(g);
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * Calculates if an item is near the player (within 50 pixels)
	 * 
	 * @return list of items
	 */
	public ArrayList<Item> getItemsNear() {
		ArrayList<Item> itemsNear = new ArrayList<Item>();

		for (Item item : itemsWorld) {
			double dist = calcDist(player.getX(), player.getY(), item.getX(), item.getY());
			if (dist <= 50)
				itemsNear.add(item);
		}
		return itemsNear;
	}

	/**
	 * Calculates if any monster is within a certain distance of the player,
	 * i.e. if it's in "attacking" range or "chasing" range
	 * 
	 * @param threshMin
	 * @param threshMax
	 * @return list of monsters
	 */
	public ArrayList<Monster> getMonstersNear(int threshMin, int threshMax) {
		ArrayList<Monster> monstersNear = new ArrayList<Monster>();

		for (Monster monster : monsters) {
			double dist = calcDist(player.getX(), player.getY(), monster.getX(), monster.getY());
			if (dist >= threshMin && dist <= threshMax)
				monstersNear.add(monster);
		}

		return monstersNear;
	}

	/**
	 * Calculates if any NPC is within 50 pixels of the player
	 * 
	 * @return list of NPCs
	 */
	public ArrayList<NPC> getNPCsNear() {
		ArrayList<NPC> npcsNear = new ArrayList<NPC>();

		for (NPC npc : npcs) {
			double dist = calcDist(player.getX(), player.getY(), npc.getX(), npc.getY());
			if (dist <= 50)
				npcsNear.add(npc);
		}

		return npcsNear;
	}

	/**
	 * Calculates the distance between two objects using Pythagorean theorem
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return distance
	 */
	public double calcDist(float x1, float y1, float x2, float y2) {
		float x = x1 - x2;
		float y = y1 - y2;

		return Math.sqrt((x * x) + (y * y));
	}
}
