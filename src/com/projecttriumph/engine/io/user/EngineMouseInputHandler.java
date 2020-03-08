package com.projecttriumph.engine.io.user;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import com.projecttriumph.engine.rendering.ScreenManager;

// TODO implement
public class EngineMouseInputHandler implements MouseInputListener, MouseWheelListener {
	private Point mousePoint;
	private Point frameMousePoint;
	
	public EngineMouseInputHandler() {
		mousePoint = new Point(0, 0);
		frameMousePoint = new Point(mousePoint);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("\n\nEngineMouseInputHandler.mouseClicked()");
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("\n\nEngineMouseInputHandler.mousePressed()");
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("\n\nEngineMouseInputHandler.mouseReleased()");
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("\n\nEngineMouseInputHandler.mouseEntered()");
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("\n\nEngineMouseInputHandler.mouseExited()");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("\n\nEngineMouseInputHandler.mouseDragged()");
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("\n\nEngineMouseInputHandler.mouseMoved()");
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO check to make sure not over GUI element with scrolling 
		ScreenManager.getInstance().getCamera().onMouseWheelEvent(e);
	}
	
	public void captureInput() {
		mousePoint = ScreenManager.getInstance().getMousePoint();
		if (mousePoint != null) {
			frameMousePoint = new Point(mousePoint);
		}
	}
	
	public Point getFrameMousePoint() {
		return this.frameMousePoint;
	}
}