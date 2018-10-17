package com.projecttriumph.engine.rendering;

import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.projecttriumph.engine.api.math.shape.Rectangle;

public class Camera {
	private static final int SCEEN_OFF_SET_PER_KEY = 5;
	private final int screenWidth;
	private final int screenHeight;
	private final int screenCenterX;
	private final int screenCenterY;
	private double scale = 1.0;
	private int zoomScale = 1;
	private int offsetX = 0;
	private int offsetY = 0;
	
	// test constructor, TODO remove
	public Camera() {
		this(0,0);
	}
	
	public Camera(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenCenterX = this.screenWidth / 2;
		this.screenCenterY = this.screenHeight / 2;
	}
	
	public void onMouseWheelEvent(MouseWheelEvent event) {
		// zoom
		int rotation = event.getWheelRotation();
		if (zoomScale == 1) {
			if (rotation < 0) { // up/away from user (zooming in)
				zoomScale -= rotation;
			} else { // down/towards from user (zooming out)
				zoomScale = -1 - rotation;
			}
		} else if (zoomScale == -1) { 
			if (rotation > 0) { // down/towards from user (zooming out)
				zoomScale = 1 - rotation;
			} else { // up/away from user (zooming in)
				zoomScale -= rotation;
			}
		} else {
			zoomScale -= event.getWheelRotation();
		}
		
		if (zoomScale < 0) {
			scale = 1.0 / Math.abs(zoomScale);
		} else if (zoomScale == 0) {
			scale = 1;
		} else {
			scale = Math.abs(zoomScale);
		}
	}
	
	public void onArrowKeyEvent(KeyEvent e) {
		// TODO tweak movement as needed.
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				this.offsetX -= SCEEN_OFF_SET_PER_KEY;
				break;
			case KeyEvent.VK_RIGHT:
				this.offsetX += SCEEN_OFF_SET_PER_KEY;
				break;
			case KeyEvent.VK_DOWN:
				this.offsetY += SCEEN_OFF_SET_PER_KEY;
				break;
			case KeyEvent.VK_UP:
				this.offsetY -= SCEEN_OFF_SET_PER_KEY;
				break;
		}
	}
	
	public AffineTransform getCurrentTransform() {
		AffineTransform at = new AffineTransform();
		// shifts the center of the scaled screen to the center of the screen to create zooming from center
		at.translate(-screenCenterX * (scale - 1), -screenCenterY * (scale - 1));
		// zooms the screen 
		at.scale(scale, scale);
		// Translates to the off set position
		at.translate(offsetX, offsetY);
		return at;
	}
	
	public boolean isRectVisible(Rectangle rect) {
		rect.scale(zoomScale).translate(offsetX, offsetY);
		return ScreenManager.getInstance().getScreenBounds().intersects(rect);
	}
	
	public boolean isRectVisible(Rectangle2D rect) {
		Rectangle r = new Rectangle(rect).scale(zoomScale).translate(offsetX, offsetY);
		return ScreenManager.getInstance().getScreenBounds().intersects(r);
	}
}