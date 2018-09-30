package com.projecttriumph.engine.gameobject.interfaces;

import com.projecttriumph.engine.math.shape.Rectangle;

public interface IBoundingBox {
	
	/**
	 * Gets the bounding box of the  object. Used for collision and drawing checks
	 * @return
	 */
	public Rectangle getBoundingBox();
	
}