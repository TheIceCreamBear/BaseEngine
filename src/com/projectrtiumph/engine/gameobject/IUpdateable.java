package com.projectrtiumph.engine.gameobject;

/**
 * Defines a functional interface IUpdateable with the method {@link IUpdateable#update(double)}.
 * Any object that implements this interface is one that can be update by the game engine, and 
 * has aspects of it that will change or need to change.
 * 
 * @author Joseph
 */
public interface IUpdateable {
	/**
	 * The method by which all updateable objects will be updated. The delta time shows how much time 
	 * has passed between updates and should be used to scale movements.
	 * @param deltaTime - the time that has passed after the last update call.
	 */
	public void update(double deltaTime);
}