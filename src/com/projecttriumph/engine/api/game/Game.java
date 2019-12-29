package com.projecttriumph.engine.api.game;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.projecttriumph.engine.api.io.user.IGameKeyInputHandler;
import com.projecttriumph.engine.api.io.user.IGameMouseInputHandler;
import com.projecttriumph.engine.rendering.Camera;

// TODO update docs
/**
 * The @Game interface is used to tag a {@link com.projecttriumph.engine.api.game.GameController GameController}
 * with game specific metadata and information that only needs to gathered at initialization.
 * @author Joseph
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Game {
	/**
	 * Lowercase, no space, id name of this game. this will be used to make sub folders in 
	 * the locations for saves, mods, other things
	 */
	String gameID();
	
	/**
	 * The proper name of this game. Can be capitol and have spaces and special characters.
	 * <p>
	 * Used for frame and process naming as well as for display in the game selection screen.
	 */
	String gameName();
	
	/**
	 * String representation of this game's version. Used for mod version checking and to allow for 
	 * different versions of the same game to exist at once.
	 */
	String version();
	
	/**
	 * An implementation of the {@link IGameMouseInputHandler} interface, defining the how the 
	 * game will handle mouse input.
	 */
	Class<? extends IGameMouseInputHandler> mouseInput();
	
	/**
	 * An implementation of the {@link IGameKeyInputHandler} interface, defining the how the 
	 * game will handle key input.
	 */
	Class<? extends IGameKeyInputHandler> keyInput();
	
	/**
	 * An implementation of the {@link Camera} class, defining the camera motion for the game.
	 * <p>
	 * If left blank, the camera will default to the camera provided by the engine. 
	 */
	Class<? extends Camera> camera() default Camera.class;
		
	/**
	 * Boolean value to determine if the game will support mods. Default is false.
	 * <p>
	 * NOTE: This is a placeholder for a possible future feature
	 */
	boolean allowMods() default false;
}