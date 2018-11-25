package com.projecttriumph.engine.io.user;

import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

/**
 * Interface defining the methods a game will need to be aware of when dealing with mouse input.
 * This interface is to handle input for the game only and not input for the gui. That will be
 * handled by the game engine.
 * 
 * @author Joseph
 */
public interface IGameMouseInputHandler extends MouseInputListener, MouseWheelListener {
	/**
	 * Called at the beginning of each update loop. Used to save the state of the mouse position 
	 * or other controls the game wants to be the same for each object that updates this update.
	 */
	public void captureInput();
}