package com.projecttriumph.engine.io.user;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.projecttriumph.engine.rendering.ScreenManager;

public class KeyInputHandler implements KeyListener {
	private boolean[] keyDown;
	protected boolean[] frameKeyDown;
	// keeps track of the keys that were pressed this frame
	private boolean[] keyPressed;
	protected boolean[] frameKeyPressed;
	
	private static KeyInputHandler instance;
	
	public KeyInputHandler() {
		this.keyDown = new boolean[600];
		this.frameKeyDown = this.keyDown.clone();
		
		this.keyPressed = new boolean[600];
		this.frameKeyPressed = this.keyPressed.clone();
		
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
		
		this.keyPressed[e.getKeyCode()] = true;
		
		this.keyDown[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		this.keyDown[e.getKeyCode()] = false;
	}
	
	public void captureInput() {
		this.frameKeyDown = this.keyDown.clone();
		this.frameKeyPressed = this.keyPressed.clone();
		this.keyPressed = new boolean[600];
	}
	
	/**
	 * A method that returns true if the key was initially pressed down this frame.
	 * 
	 * <p> This method only returns true if the key was physically pressed down this frame.
	 * If you want to know if the key is still pressed down, or want to check if the key is 
	 * pressed each frame, use {@link #isKeyDown(int)}
	 * 
	 * @param keyCode - the key code
	 * @return true if the key was initially pressed down this frame
	 */
	public static boolean wasKeyPressedThisFrame(int keyCode) {
		return getInstance().frameKeyPressed[keyCode];
	}
	
	/**
	 * A method that returns true if the passed in key is down this frame.
	 * 
	 * <p> This method returns true for each frame the key is pressed down. 
	 * If you want to know when the key was initially pressed, use {@link #wasKeyPressedThisFrame(int)}
	 * 
	 * @param keyCode - the key code
	 * @return true if the key is pressed down this frame
	 */
	public static boolean isKeyDown(int keyCode) {
		return getInstance().frameKeyDown[keyCode];
	}
	
	public static KeyInputHandler getInstance() {
		return instance;
	}
}