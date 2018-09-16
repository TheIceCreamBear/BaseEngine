package com.projectrtriumph.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		try {
			test = ImageIO.read(new File("C:/Users/Joseph/Desktop/Gradient.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		// TODO Remove this debug code
		if (this.keyHandler.isKeyDown(KeyEvent.VK_ESCAPE)) {
			System.exit(0);
		}
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
	
	int asdfg;
	int dg;
	BufferedImage test;
	
	// TODO implement
	private void render(Graphics2D g) {
		// Black out the screen to prevent old stuff from showing
		g.fillRect(0, 0, screenManager.getScreenWidth(), screenManager.getScreenHeight());
		AffineTransform saveState = g.getTransform();
		g.transform(ScreenManager.getInstance().getCamera().getCurrentTransform());
		
		// RENDER UPDATEABLE
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
				g.setColor(new Color(asdfg, 100, 100));
				g.drawImage(test, 0, 0, null);
				g.fillRect(500, 200, 20, 20);
				g.fillRect(3000, 1000, 20, 20);
				g.fillRect(2800, 200, 20, 20);
				g.fillRect(500, 2015, 20, 20);
				g.fillRect(1500, 1000, 20, 20);
				g.fillRect(500, 500, 20, 20);
				
				break;
			case PAUSE_MENU:
				break;
			case GAME:
				break;
			default:
				break;
		}
		
		// REDNER STATIC GUI
		g.setTransform(saveState);
	}
	
	private void run() {
		this.engineState = EnumEngineState.RUNNING;
		// ALL TIME VARS ARE IN NANO TIME
//		int counter = 0;
//		final double FRAMES_PER_SECOND = 60;
//		final double NS_PER_FRAME = 1000000000 / FRAMES_PER_SECOND;
		
		while (this.engineState == EnumEngineState.RUNNING) {
//			double startTime = System.nanoTime();
			
			captureInput();
			
			update();
			
			// RENDER
			Graphics2D g = screenManager.getRenderGraphics();
			render(g);
			g.dispose();
			screenManager.updateDisplay(); // SYNCS SCREEN WITH VSync
			// END RENDER
			
			// THE FOLLOWING IS BELIEVED TO NOT BE NECESSARY AS THE WAY THE FRAME RENDERS WILL CAP THE FPS AT THE MONITOR'S REFRESH RATE
			
			
			// SLEEP A BIT
//			double stopTime = System.nanoTime();
//			double duration = stopTime - startTime;
//			double sleepTime = NS_PER_FRAME - duration;
//			try {
//				if (sleepTime < 0) {
//					System.err.println("RUNNING BEHIND!!!");
//					System.out.println(duration);
//					System.out.println(sleepTime);
//					System.out.println(NS_PER_FRAME);
//					System.out.println(counter);
//				} else {
//					Thread.sleep((long) sleepTime / 1000000);
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
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