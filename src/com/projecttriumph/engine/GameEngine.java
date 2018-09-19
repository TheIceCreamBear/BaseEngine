package com.projecttriumph.engine;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.projecttriumph.engine.io.user.KeyInputHandler;
import com.projecttriumph.engine.io.user.MouseInputHandler;
import com.projecttriumph.engine.rendering.ScreenManager;

public final class GameEngine {
	public enum EnumEngineState {
		INVALID, INITIALIZING, RUNNING, STOPPING;
	}
	
	public enum EnumEngineRenderState {
		SPLASH, MENU, GAME, PAUSE_MENU;
	}
	
	private enum EnumUpdateSkipping {
		NO_SKIP, // for when the refresh rate is 60
		TWO_UPDATES_PER_FRAME, // for when the refresh rate is 30
		TWO_FRAMES_PER_UPDATE, // for when the refresh rate is 120
		FOUR_FRAMES_PER_UPDATE; // for the RARE case that the refresh rate is 240
	}
	
	/**
	 * Instance for the GameEngine
	 */
	private static GameEngine instance;
	
	// States
	private EnumEngineState engineState = EnumEngineState.INVALID;
	private EnumEngineRenderState renderState = EnumEngineRenderState.SPLASH;
	private final EnumUpdateSkipping skipping;
	
	// Rendering vars
	private ScreenManager screenManager;
	private int ticks = 0;
	/**
	 * Used to determine how many frames must be rendered before the next update.
	 * Only used for {@link EnumUpdateSkipping#TWO_UPDATES_PER_FRAME} & 
	 * {@link EnumUpdateSkipping#FOUR_FRAMES_PER_UPDATE}
	 */
	private int framesTillUpdate;
	
	// Input Handlers
	private KeyInputHandler keyHandler;
	private MouseInputHandler mouseHandler;
	
	// TODO temp
	BufferedImage img;
	
	public GameEngine(ScreenManager screenManager) {
		this.screenManager = screenManager;
		switch (this.screenManager.getRefreshRate()) {
			case 30:
				this.skipping = EnumUpdateSkipping.TWO_UPDATES_PER_FRAME;
				break;
			case 60:
				this.skipping = EnumUpdateSkipping.NO_SKIP;
				break;
			case 120:
				this.skipping = EnumUpdateSkipping.TWO_FRAMES_PER_UPDATE;
				this.framesTillUpdate = 1;
				break;
			case 240:
				this.skipping = EnumUpdateSkipping.FOUR_FRAMES_PER_UPDATE;
				this.framesTillUpdate = 3;
				break;
			default:
				this.skipping = EnumUpdateSkipping.NO_SKIP;
				// TODO Remove support for nonlisted monitor refresh rates
				break;
		}
		try {
			this.img = ImageIO.read(new File("Gradient.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		captureInput();
		
		// TODO: implement thread splitting
		
		// render
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
		ticks++;
	}
	
	// TODO implement
	private void render(Graphics2D g) {
		// Black out the screen to prevent old stuff from showing
		AffineTransform saveState = g.getTransform();
		g.transform(ScreenManager.getInstance().getCamera().getCurrentTransform());
		g.fillRect(0, 0, screenManager.getScreenWidth(), screenManager.getScreenHeight());
		
		// RENDER UPDATEABLE
		switch (renderState) {
			case SPLASH:
				break;
			case MENU:
				g.drawImage(img, 0, 0, null);
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
		
		long timer = System.currentTimeMillis();
		
		while (this.engineState == EnumEngineState.RUNNING) {	
			// system to make sure there is always 60 UPS
			switch (this.skipping) {
				case FOUR_FRAMES_PER_UPDATE:
					if (this.framesTillUpdate == 0) {
						this.update();
						this.framesTillUpdate = 3;
					} else {
						this.framesTillUpdate--;
					}
					break;
				case NO_SKIP:
					update();
					break;
				case TWO_FRAMES_PER_UPDATE:
					if (this.framesTillUpdate == 0) {
						this.update();
						this.framesTillUpdate = 2;
					} else {
						this.framesTillUpdate--;
					}
					break;
				case TWO_UPDATES_PER_FRAME:
					update();
					update();
					break;
				default:
					break;
			}
			
			// RENDER
			Graphics2D g = screenManager.getRenderGraphics();
			render(g);
			g.dispose();
			screenManager.updateDisplay(); // SYNCS SCREEN WITH VSync
			// END RENDER
			
			if (System.currentTimeMillis() - timer > 1000) {
				System.out.println(ticks + "TPS");
				ticks = 0;
				timer = System.currentTimeMillis();
			}
		}
		
		this.engineState = EnumEngineState.STOPPING;
	}
	
	public EnumEngineState getEngineState() {
		return this.engineState;
	}

	public static GameEngine getInstance() {
		return instance;
	}
}