package com.projecttriumph.engine.api.io.user;

import java.awt.event.KeyListener;

import com.projecttriumph.engine.io.user.KeyInputHandler;

/**
 * Interface defining the methods a game will need to be aware of when dealing with key input.
 * <p>
 * This interface is to handle input for the game only and not input for the gui. That will be
 * handled by the game engine. Furthermore, for individual key presses, a game should use
 * {@link KeyInputHandler#isKeyDown(int)}. This interface is only for actions that must happen 
 * on a key press like the showing of a menu.
 * 
 * @author Joseph
 */

// TODO decide if on key actions like those for showing a menu need this class or just an Action Class
public interface IGameKeyInputHandler extends KeyListener {
	
}