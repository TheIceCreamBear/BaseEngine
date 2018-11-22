package com.projecttriumph.engine.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.projecttriumph.engine.rendering.Camera;

// TODO document
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Game {
	/**
	 * Lowercase, no space, id name of this game. this will be used to make sub folders in the locations for
	 * saves, mods, other things
	 */
	String gameID();
	
	/**
	 * The proper name of this game. Can be capitol and have spaces and special characters.
	 * <p>
	 * Used for frame and process naming as well as for display in the game selection screen.
	 */
	String gameName();
	
	/**
	 * String representation of this game. Used for mod version checking and to allow for 
	 * different versions of the same game to exist at once.
	 */
	String version();
	
	/**
	 * A fully qualified class name referring to the class that is a subclass of {@link Camera}
	 * for this game. If pointed at a class that does not extend {@link Camera}, 
	 * the engine will crash.
	 * <p>
	 * The default value is an empty string. If left empty, default functionality will be used. 
	 */
	String camera() default "com.projecttriumph.engine.rendering.Camera";
	
	/**
	 * Boolean value to determine if the game will support mods. Default is false.
	 */
	boolean allowMods() default false;
}