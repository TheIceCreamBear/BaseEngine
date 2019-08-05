package com.projecttriumph.engine.api.gui;

public interface IGuiButton extends IGuiElement {
	/**
	 * Called when the implementing gui element gets clicked. Passes in the 
	 * location where in this element the mouse was clicked.
	 * @param localMouseX - the x pos of the mouse in the element
	 * @param localMouseY - the y pos of the mouse in the element
	 */
	public void onClick(double localMouseX, double localMouseY);
}