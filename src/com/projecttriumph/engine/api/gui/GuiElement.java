package com.projecttriumph.engine.api.gui;

import com.projecttriumph.engine.api.math.shape.Rectangle;

/**
 * Top level class for the GuiSystem. Defines the basic focus and location behavior
 * @author Joseph
 *
 */
public abstract class GuiElement implements IGuiElement {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	private boolean focused;
	
	public GuiElement(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.focused = false;
	}
	

	@Override
	public final Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}

	@Override
	public final boolean hasFocus() {
		return focused;
	}

	@Override
	public final void focus() {
		this.focused = true;
		this.onFocusChange(focused);
	}

	@Override
	public final void removeFocus() {
		this.focused = false;
		this.onFocusChange(focused);
	}
}