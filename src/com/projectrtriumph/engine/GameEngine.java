package com.projectrtriumph.engine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.projectrtriumph.engine.io.user.KeyInputHandler;
import com.projectrtriumph.engine.io.user.MouseInputHandler;
import com.projectrtriumph.engine.rendering.ScreenManager;

public final class GameEngine {
	public enum EnumEngineState {
		INVALID, INITIALIZING, RUNNING, STOPPING;
	}
	
	public enum EnumEngineRenderState {
		SPLASH, MENU, GAME, PAUSE_MENU;
	}
	
	/**
	 * instance for the GameEngine
	 */
	private static GameEngine instance;
	
	// States
	private EnumEngineState engineState = EnumEngineState.INVALID;
	private EnumEngineRenderState renderState = EnumEngineRenderState.SPLASH;
	
	// Rendering vars
	private ScreenManager screenManager;
	
	// Input Handlers
	private KeyInputHandler keyHandler;
	private MouseInputHandler mouseHandler;
	
	public GameEngine(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}
	
	public void startEngine() {
		this.initialize();
		this.run();
	}
	
	private void initialize() {
		instance = this;
		this.engineState = EnumEngineState.INITIALIZING;
		
		// INPUT
		this.keyHandler = new KeyInputHandler();
		this.mouseHandler = new MouseInputHandler();
		this.screenManager.addInputListeners(keyHandler, mouseHandler);
		
		this.renderState = EnumEngineRenderState.MENU;
		System.gc();
	}
	
	// TODO implement
	private void captureInput() {
		this.keyHandler.captureInput();
		this.mouseHandler.captureInput();
	}

	// TODO implement
	private void update() {
		switch (renderState) {
			case SPLASH:
				break;
			case MENU:
				break;
			case PAUSE_MENU:
				break;
			case GAME:
				break;
			default:
				break;
		}
	}
	
	int asdf;
	int asdfg;
	int dg;
	
	// TODO implement
	private void render(Graphics2D g) {
		// Black out the screen to prevent old stuff from showing
		g.fillRect(0, 0, screenManager.getScreenWidth(), screenManager.getScreenHeight());
		switch (renderState) {
			case SPLASH:
				break;
			case MENU:
				if (asdfg == 255) {
					dg = -1;
				} else if (asdfg == 0) {
					dg = 1;
				}
				asdfg += dg;
				g.setColor(new Color(asdfg, asdfg, asdfg));
				g.fillRect(asdf++, 200, 20, 20);
				break;
			case PAUSE_MENU:
				break;
			case GAME:
				break;
			default:
				break;
		}
	}
	
	private void run() {
		this.engineState = EnumEngineState.RUNNING;
		// ALL TIME VARS ARE IN MILI TIME
		final double TICKS_PER_SECOND = 60;
		final double MS_PER_FRAME = 1000 / TICKS_PER_SECOND;
		
		while (this.engineState == EnumEngineState.RUNNING) {
			double startTime = System.currentTimeMillis();
			
			captureInput();
			
			update();
			
			// RENDER
			Graphics2D g = screenManager.getRenderGraphics();
			render(g);
			g.dispose();
			screenManager.updateDisplay();
			// END RENDER
			
			// SLEEP A BIT
			double sleepTime = MS_PER_FRAME + startTime - System.currentTimeMillis();
			try {
				if (sleepTime < 0) {
					System.err.println("RUNNING BEHIND!!!");
				} else {
					Thread.sleep((long) sleepTime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		this.engineState = EnumEngineState.STOPPING;
	}

	public static GameEngine getInstance() {
		return instance;
	}

	public EnumEngineState getEngineState() {
		return this.engineState;
	}
}