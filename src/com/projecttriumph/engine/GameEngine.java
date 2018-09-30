package com.projecttriumph.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.projecttriumph.engine.io.user.KeyInputHandler;
import com.projecttriumph.engine.io.user.MouseInputHandler;
import com.projecttriumph.engine.math.MathHelper;
import com.projecttriumph.engine.rendering.ScreenManager;

public final class GameEngine {
	public enum EnumEngineState {
		INVALID, INITIALIZING, RUNNING, STOPPING;
	}
	
	public enum EnumEngineRenderState {
		SPLASH, MENU, GAME, PAUSE_MENU;
	}
	
	public enum EnumLockedFrameRate {
		NOT_INIT,
		NO,
		YES_30,
		YES_60,
		YES_120,
		YES_240;
	}
	
	/**
	 * Instance for the GameEngine
	 */
	private static GameEngine instance;
	
	// States
	private EnumEngineState engineState = EnumEngineState.INVALID;
	private EnumEngineRenderState renderState = EnumEngineRenderState.SPLASH;
	private EnumLockedFrameRate frameRateType = EnumLockedFrameRate.NOT_INIT;
	
	// Rendering vars
	private ScreenManager screenManager;
	private int ticks = 0;
	/**
	 * Used to determine how many frames must be rendered before the next update.
	 * Only used for {@link EnumUpdateSkipping#TWO_UPDATES_PER_FRAME} & 
	 * {@link EnumUpdateSkipping#FOUR_FRAMES_PER_UPDATE}
	 */
	private int framesTillUpdate;
	private int updatesTillNonRound;
	
	// Input Handlers
	private KeyInputHandler keyHandler;
	private MouseInputHandler mouseHandler;
	
	// TODO temp
	BufferedImage img;
	
	public GameEngine(ScreenManager screenManager) {
		this.screenManager = screenManager;
		try {
			this.img = ImageIO.read(new File("Gradient.png"));
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
				g.setColor(Color.BLUE);
				g.fillRect(400, 400, 200, 200);
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
		double totalElapsed = 0;
		int updatesTillInit = 81;
		final double numberTimesRun = updatesTillInit - 21;
		final double _30hz = 1000.0 / 30;
		final double _60hz = 1000.0 / 60;
		final double _120hz = 1000.0 / 120;
		final double _240hz = 1000.0 / 240;
		final double TIMER_PER_FRAME = _60hz;
		
		long timer = System.currentTimeMillis();
		long start = System.currentTimeMillis();
		
		while (this.engineState == EnumEngineState.RUNNING) {
			start = System.currentTimeMillis();
			// system to make sure there is always 60 UPS
			switch (this.frameRateType) {
				case YES_30:
					update();
					update();
					break;
				// NOT_INIT AND NO WILL RUN AT A NORMAL SPEED WHICH IS EQUAL TO 60TPS
				case NOT_INIT:
					if (updatesTillInit > 0) {
						updatesTillInit--;
					}
				case NO:
				case YES_60:
					update();
					break;
				case YES_120:
					if (this.framesTillUpdate == 0) {
						this.update();
						this.framesTillUpdate = 2;
					} else {
						this.framesTillUpdate--;
					}
					break;
				case YES_240:
					if (this.framesTillUpdate == 0) {
						this.update();
						this.framesTillUpdate = 3;
					} else {
						this.framesTillUpdate--;
					}
					break;
				default:
					System.err.println("INVALID STATE!!!!");
					
					break;
			}
			
			// RENDER
			Graphics2D g = screenManager.getRenderGraphics();
			render(g);
			g.dispose();
			screenManager.updateDisplay(); // SYNCS SCREEN WITH VSync
			// END RENDER
			long elapsed = System.currentTimeMillis() - start;
			if (this.frameRateType == EnumLockedFrameRate.NOT_INIT) {
				if (!(updatesTillInit >= numberTimesRun)) {
					totalElapsed += elapsed;
				}
				if (updatesTillInit == 0) {
					double averageElapsed = totalElapsed / numberTimesRun;
					System.err.println(averageElapsed);
					System.out.println(_60hz);
					if (MathHelper.equal(averageElapsed, _30hz, 0.001)) {
						this.frameRateType = EnumLockedFrameRate.YES_30;
					} else if (MathHelper.equal(averageElapsed, _60hz, 0.001)) {
						this.frameRateType = EnumLockedFrameRate.YES_60;
					} else if (MathHelper.equal(averageElapsed, _120hz, 0.001)) {
						this.frameRateType = EnumLockedFrameRate.YES_120;
					} else if (MathHelper.equal(averageElapsed, _240hz, 0.001)) {
						this.frameRateType = EnumLockedFrameRate.YES_240;
					} else {
						this.frameRateType = EnumLockedFrameRate.NO;
						this.updatesTillNonRound = 2;
					}
					System.err.println(this.frameRateType);
				}
			}
			
			if (this.frameRateType == EnumLockedFrameRate.NO) {
				long sleepTime;
				if (this.updatesTillNonRound == 0) {
					sleepTime = (long) (TIMER_PER_FRAME - elapsed);
					this.updatesTillNonRound = 2;
				} else {
					sleepTime = Math.round(TIMER_PER_FRAME - elapsed);
					this.updatesTillNonRound--;
				}

				if (sleepTime >= 0) {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
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