package com.projecttriumph.engine.api.gui;

public interface IGuiText extends IGuiElement {
	
	/**
	 * Used by the engine to determine the number of characters to display per row, or when to scroll horizontally
	 * if only one row is allowed. Minimum value of 1, or zero for a dynamic number of columns.
	 * <p>
	 * This value will override the width set in the constructor. A value of 0 will prevent this.
	 * @return - the number
	 */
	public int getColumns();
	
	/**
	 * Used by the engine to determine the number of rows to display at once, or when to create scrolling when there 
	 * are more rows than the height will be able to display. Minimum value of 1
	 * @return
	 */
	public int getRows();
	
	/**
	 * Gets the current text of the field as a string. The internal representation of the 
	 * text may be a string builder, a char array, or a char ArrayList, as long as this method returns a string.
	 * @return - the text of the field
	 */
	public String getText();
	
	/**
	 * Sets the text of the text element.
	 * @param newText - the new text
	 */
	public void setText(String newText);
}