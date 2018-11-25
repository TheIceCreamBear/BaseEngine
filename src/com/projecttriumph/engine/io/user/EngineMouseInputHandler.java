package com.projecttriumph.engine.io.user;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import com.projecttriumph.engine.rendering.ScreenManager;

// TODO implement
public class EngineMouseInputHandler implements MouseInputListener, MouseWheelListener {
	public EngineMouseInputHandler() {
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO check to make sure not over GUI element with scrolling 
		ScreenManager.getInstance().getCamera().onMouseWheelEvent(e);
	}
}