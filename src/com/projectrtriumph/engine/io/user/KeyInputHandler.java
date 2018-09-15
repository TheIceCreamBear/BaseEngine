package com.projectrtriumph.engine.io.user;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputHandler implements KeyListener {
	private boolean[] keyDown;
	private boolean[] frameKeyDown;
	
	private static KeyInputHandler instance;
	
	public KeyInputHandler() {
		this.keyDown = new boolean[600];
		this.frameKeyDown = this.keyDown.clone();
		
		instance = this;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
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
	
	public static KeyInputHandler getInstance() {
		return instance;
	}
}