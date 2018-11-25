package com.projecttriumph.engine.api.interfaces;

import java.awt.Graphics2D;

/**
 * Defines a functional interface IDrawable with the method {@link IDrawable#draw(Graphics2D)}. 
 * Any object that implements this interface is one that can be drawn onto the screen, 
 * including objects that don't update and objects that do update.
 * 
 * @author Joseph
 */
public interface IDrawable extends IBoundingBox {
	/**
	 * The method that will be called by the engine to draw each and every object that can
	 * be seen on the screen.
	 * @param g - the graphics object that the implementing object will be drawing onto.
	 */
	public void draw(Graphics2D g);
}