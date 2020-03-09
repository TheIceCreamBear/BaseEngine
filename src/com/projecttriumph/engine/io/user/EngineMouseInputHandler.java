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
		System.out.println("EngineMouseInputHandler.mouseClicked()");
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("EngineMouseInputHandler.mousePressed()");
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("EngineMouseInputHandler.mouseReleased()");
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("EngineMouseInputHandler.mouseEntered()");
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("EngineMouseInputHandler.mouseExited()");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("EngineMouseInputHandler.mouseDragged()");
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("EngineMouseInputHandler.mouseMoved()");
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