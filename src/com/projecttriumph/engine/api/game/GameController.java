package com.projecttriumph.engine.api.game;

import java.awt.Graphics2D;

/**
 * A Game Controller is an interface that handles running the game. The controller
 * has three main functions: Initialize the game elements, update the game elements,
 * and draw the game elements. The implementing class should define its own method
 * of storing, adding, removing, and manipulating the game elements. The Implementing 
 * class MUST also have the {@link com.projecttriumph.engine.api.game.Game @Game} 
 * annotation.
 * @author Joseph
 *
 */
public interface GameController {
	/**
	 * Initializes the game and its content. Any creation of objects that will be used 
	 * throughout the game should be created here. Any controllers, storage objects, 
	 * gui handlers, event threads, state handlers, etc should be created in this method.
	 */
	public void initialize();
	
	/**
	 * Updates the contents of the game. Assume for all updates that there are 60 UPS,
	 * independent on the framerate of the engine. The only exception to this will occur
	 * when the update and rendering process takes longer than 1s/framerate.
	 */
	public void updateGame();
	
	/**
	 * Renders the contents of the game. The controller is responsible for determining 
	 * what gets rendered, including all game objects and any GUI elements not handled
	 * by a GUIController.
	 * @param g - The graphics object to render the current frame to
	 */
	public void renderGame(Graphics2D g);
}