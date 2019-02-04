
/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Kamil Jakrzewski (kjakrzewski)
 */

import org.newdawn.slick.SlickException;

/**
 * Represents the camera that controls our viewpoint.
 */
public class Camera {

	/** The unit this camera is following */
	private Player unitFollow;

	/** The width and height of the screen */
	/** Screen width, in pixels. */
	public final int screenwidth;
	/** Screen height, in pixels. */
	public final int screenheight;

	/** The camera's position in the world, in x and y coordinates. */
	private int xPos;
	private int yPos;

	/** Returns the camera's x coordinate in world */
	public int getxPos() {
		return xPos;
	}

	/** Returns the camera's y coordinate in world */
	public int getyPos() {
		return yPos;
	}

	/**
	 * Create a new Camera object.
	 * 
	 * @throws SlickException
	 */
	public Camera(Player player, int screenwidth, int screenheight) throws SlickException {
		followUnit(player);
		this.screenwidth = screenwidth;
		this.screenheight = screenheight;
	}

	/**
	 * Update the game camera to recentre it's viewpoint around the player
	 */
	public void update() throws SlickException {
		xPos = (int) unitFollow.getX() - (screenwidth/2);
		yPos = (int) unitFollow.getY() - (screenheight/2);
	}

	/**
	 * Returns the minimum x value on screen
	 */
	public int getMinX() {
		return xPos;
	}

	/**
	 * Returns the maximum x value on screen
	 */
	public int getMaxX() {
		return xPos + screenwidth;
	}

	/**
	 * Returns the minimum y value on screen
	 */
	public int getMinY() {
		return yPos;
	}

	/**
	 * Returns the maximum y value on screen
	 */
	public int getMaxY() {
		return yPos + screenheight;
	}

	/**
	 * Tells the camera to follow a given unit.
	 */
	public void followUnit(Object unit) throws SlickException {
		unitFollow = (Player) unit;
		xPos = (int) unitFollow.getX();
		yPos = (int) unitFollow.getY();

	}

}