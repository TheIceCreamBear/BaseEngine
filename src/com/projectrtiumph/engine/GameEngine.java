package com.projectrtiumph.engine;

import java.awt.Graphics2D;

import com.projectrtiumph.engine.rendering.ScreenManager;

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
	
	public GameEngine(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}
	
	private void initialize() {
		instance = this;
		this.engineState = EnumEngineState.INITIALIZING;
		
		
		System.gc();
	}
	
	private void captureInput() {
		
	}
	
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
	
	private void render(Graphics2D g) {
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
				Thread.sleep((long) sleepTime);
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