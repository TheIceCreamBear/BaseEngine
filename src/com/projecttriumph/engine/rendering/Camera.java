package com.projecttriumph.engine.rendering;

import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.projecttriumph.engine.api.math.shape.Rectangle;
import com.projecttriumph.engine.io.user.KeyInputHandler;

public class Camera {
	private static final int SCEEN_OFF_SET_PER_KEY = 5;
	protected final int screenWidth;
	protected final int screenHeight;
	protected final int screenCenterX;
	protected final int screenCenterY;
	protected double scale = 1.0;
	protected int zoomScale = 1;
	protected int offsetX = 0;
	protected int offsetY = 0;
	
	// test constructor, remove
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
				zoomScale -= rotation;
			} else { // up/away from user (zooming in)
				zoomScale = 1 - rotation;
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
	
	public void frameMoveCamera() {
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_LEFT)) {
			this.offsetX += SCEEN_OFF_SET_PER_KEY;
		}
		
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_RIGHT)) {
			this.offsetX -= SCEEN_OFF_SET_PER_KEY;
		}
		
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_DOWN)) {
			this.offsetY -= SCEEN_OFF_SET_PER_KEY;
		}
		
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_UP)) {
			this.offsetY += SCEEN_OFF_SET_PER_KEY;
		}
	}
	
	public void onArrowKeyEvent(KeyEvent e) {
		// TODO allow for dynamic key binding
//		switch(e.getKeyCode()) {
//			case KeyEvent.VK_LEFT:
//				this.offsetX -= SCEEN_OFF_SET_PER_KEY;
//				break;
//			case KeyEvent.VK_RIGHT:
//				this.offsetX += SCEEN_OFF_SET_PER_KEY;
//				break;
//			case KeyEvent.VK_DOWN:
//				this.offsetY += SCEEN_OFF_SET_PER_KEY;
//				break;
//			case KeyEvent.VK_UP:
//				this.offsetY -= SCEEN_OFF_SET_PER_KEY;
//				break;
//		}
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
	
	public void resetOffset() {
		this.offsetX = 0;
		this.offsetY = 0;
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