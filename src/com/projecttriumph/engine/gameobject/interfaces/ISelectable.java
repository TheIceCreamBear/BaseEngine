package com.projecttriumph.engine.gameobject.interfaces;

public interface ISelectable extends IBoundingBox {
	/**
	 * Sets weather or not the object is selected
	 * @param selected - the internal value of selected
	 */
	public void setSelected(boolean selected);
}