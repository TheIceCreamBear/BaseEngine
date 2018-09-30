package com.projecttriumph.engine.gameobject.interfaces;

/**
 * Defines a functional interface IUpdateable with the method {@link IUpdateable#update(double)}.
 * Any object that implements this interface is one that can be update by the game engine, and 
 * has aspects of it that will change or need to change.
 * 
 * @author Joseph
 */
public interface IUpdateable {
	/**
	 * The method by which all updateable objects will be updated. All updates will take the same amount of time to process
	 */
	public void update();
}