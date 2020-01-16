package com.projecttriumph.engine.io.user;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.projecttriumph.engine.rendering.ScreenManager;

public class EngineKeyInputHandler implements KeyListener {
	private boolean[] keyDown;
	protected boolean[] frameKeyDown;
	
	private static EngineKeyInputHandler instance;
	
	public EngineKeyInputHandler() {
		this.keyDown = new boolean[600];
		this.frameKeyDown = this.keyDown.clone();
		
		instance = this;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
			ScreenManager.getInstance().getCamera().onArrowKeyEvent(e);
		}
		
		this.keyDown[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		this.keyDown[e.getKeyCode()] = false;
	}
	
	public void captureInput() {
		this.frameKeyDown = this.keyDown.clone();
	}
	
	public boolean isKeyDown(int keyCode) {
		return this.frameKeyDown[keyCode];
	}
	
	public static EngineKeyInputHandler getInstance() {
		return instance;
	}
}