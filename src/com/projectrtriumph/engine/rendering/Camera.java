package com.projectrtriumph.engine.rendering;

import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.projectrtriumph.engine.math.shape.Rectangle;

public class Camera {
	private final int screenWidth;
	private final int screenHeight;
	private final int screenCenterX;
	private final int screenCenterY;
	private int zoomScale = 1;
	private int offsetX = 0;
	private int offsetY = 0;
	
	public Camera(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenCenterX = this.screenWidth / 2;
		this.screenCenterY = this.screenHeight / 2;
	}
	
	public void onMouseWheelEvent(MouseWheelEvent event) {
		zoomScale -= event.getWheelRotation();
		zoomScale = Math.max(1, zoomScale);
	}
	
	public void onArrowKeyEvent(KeyEvent e) {
		// TODO tweak movement as needed.
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				this.offsetX -= 2;
				break;
			case KeyEvent.VK_RIGHT:
				this.offsetX += 2;
				break;
			case KeyEvent.VK_DOWN:
				this.offsetY += 2;
				break;
			case KeyEvent.VK_UP:
				this.offsetY -= 2;
				break;
		}
	}
	
	public AffineTransform getCurrentTransform() {
		AffineTransform at = new AffineTransform();
		// shifts the center of the scaled screen to the center of the screen to create zooming from center
		at.translate(-screenCenterX * (zoomScale - 1), -screenCenterY * (zoomScale - 1));
		// zooms the screen (currently only in)
		at.scale(zoomScale, zoomScale);
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