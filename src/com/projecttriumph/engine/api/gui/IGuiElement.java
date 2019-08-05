package com.projecttriumph.engine.api.gui;

import java.awt.Graphics2D;

import com.projecttriumph.engine.api.math.shape.Rectangle;

public interface IGuiElement {
	/**
	 * A boolean value representing if this gui has elements that need to be updated
	 * such as a cool down or an animation
	 * @return true if this gui has elements that need to be updated
	 */
	public boolean hasDynamicElements();
	
	/**
	 * Called in the update loop, updates any graphical elements that need to be updated
	 * such as a cool down or an animation. Will only be called if 
	 * {@link IGuiElement#hasDynamicElements() hasDynamicElements()} returns true.
	 */
	public void updateElements();
	
	/**
	 * Draws the specified gui element and its components. Any elements drawn out side 
	 * the bounding box returned by {@link IGuiElement#getBoundingBox() getBoundingBox()} will not be shown.
	 * 
	 * @param g - The graphics object to draw this element onto
	 */
	public void draw(Graphics2D g);
	
	/**
	 * The bounding box of this gui element. This should be the biggest size of this element. 
	 * Anything out side this bounding box will not be drawn.
	 * 
	 * @return - the bounding box of this gui element
	 */
	public Rectangle getBoundingBox();
	
	/**
	 * value determining if this element has the focus of the gui system.
	 * Useful for when it's appearance changes if it does/doesnt have focus.
	 * Ex. A TextBox's blinking cursor will turn off when it looses focus
	 * 
	 * @return - boolean determining if this element is focused.
	 */
	public boolean hasFocus();
	
	/**
	 * Focuses this gui element. its sets that this element has focus and calls {@link #onFocusChange(boolean)}
	 * to let the element make modifications to its self
	 */
	public void focus();
	
	
	/**
	 * Removes the focus of this gui element and calls {@link #onFocusChange(boolean)}
	 */
	public void removeFocus();
	
	/**
	 * called when the focus state of this element changes. 
	 * use this method if you need to close other sub elements or modify the state of the element when the 
	 * focus changes
	 * 
	 * @param newState - the new focus state of this gui element
	 */
	public void onFocusChange(boolean newState);
}