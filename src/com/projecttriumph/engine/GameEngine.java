package com.projecttriumph.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.projecttriumph.engine.api.io.user.IGameKeyInputHandler;
import com.projecttriumph.engine.api.io.user.IGameMouseInputHandler;
import com.projecttriumph.engine.api.math.MathHelper;
import com.projecttriumph.engine.gamediscover.GameContainer;
import com.projecttriumph.engine.io.user.EngineMouseInputHandler;
import com.projecttriumph.engine.io.user.KeyInputHandler;
import com.projecttriumph.engine.rendering.FrameStats;
import com.projecttriumph.engine.rendering.ScreenManager;

public final class GameEngine {
	public static final boolean USE_FRAME_STATS = true;
	
	public enum EnumEngineState {
		INVALID, INITIALIZING, RUNNING, STOPPING;
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
	private EnumLockedFrameRate frameRateType = EnumLockedFrameRate.NOT_INIT;
	
	// Rendering vars
	private ScreenManager screenManager;
	private int ticks = 0;
	
	// Frame Stats vars
	private long tickCount = 0;
	private FrameStats currentFrame;
	private ArrayList<FrameStats> statsList = new ArrayList<FrameStats>();
	
	/**
	 * Used to determine how many frames must be rendered before the next update.
	 * Only used for {@link EnumLockedFrameRate#YES_120} & 
	 * {@link EnumLockedFrameRate#YES_240}
	 */
	private int framesTillUpdate;
	private int updatesTillNonRound;
	
	// Input Handlers
	private KeyInputHandler keyHandler;
	private EngineMouseInputHandler engineMouseHandler;
	private IGameMouseInputHandler gameMouseHandler;
	private IGameKeyInputHandler gameKeyHandler;
	
	private GameContainer game;
	
	public GameEngine(ScreenManager screenManager, GameContainer game) {
		this.screenManager = screenManager;
		this.game = game;
		
	}
	
	public void startEngine() {
		this.initialize();
		this.run();
		// system exit here calls shutdown threads and closes the awt threads
		System.exit(0);
	}
	
	private void initialize() {
		instance = this;
		this.engineState = EnumEngineState.INITIALIZING;
		
		// INPUT
		this.keyHandler = new KeyInputHandler();
		this.engineMouseHandler = new EngineMouseInputHandler();
		this.screenManager.addInputListeners(keyHandler, engineMouseHandler);
		
		try {
			this.gameKeyHandler = this.game.getGame().keyInput().newInstance();
			this.screenManager.addGameKeyListener(gameKeyHandler);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		try {
			this.gameMouseHandler = this.game.getGame().mouseInput().newInstance();
			this.screenManager.addGameMouseListener(gameMouseHandler);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		this.game.getController().initialize();
		
		
		System.gc();
	}
	
	// TODO implement
	private void captureInput() {
		this.keyHandler.captureInput();
		this.engineMouseHandler.captureInput();
		
		// TODO Remove this debug code
		if (KeyInputHandler.isKeyDown(KeyEvent.VK_ESCAPE)) {
			this.engineState = EnumEngineState.STOPPING;
		}
	}

	// TODO implement
	private void update() {
		if (USE_FRAME_STATS) {
			this.currentFrame.updateStart += System.nanoTime();
		}
		captureInput();
		screenManager.getCamera().frameMoveCamera();
		
		// TODO: implement thread splitting
		this.game.getController().updateGame();
		
		// update
		ticks++;
		if (USE_FRAME_STATS) {
			this.currentFrame.updateEnd += System.nanoTime();
		}
	}
	
	// TODO implement
	private void render(Graphics2D g) {
		if (USE_FRAME_STATS) {
			this.currentFrame.drawStart = System.nanoTime();
		}
		// Black out the screen to prevent old stuff from showing
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight());
		
		// Set the transform for zoom to draw the zoomed stuffs
		AffineTransform saveState = g.getTransform();
		AffineTransform cameraTransform = ScreenManager.getInstance().getCamera().getCurrentTransform();
		g.transform(cameraTransform);
		
		// RENDER UPDATEABLE
		this.game.getController().renderGame(g);
		
		// REDNER STATIC GUI
		g.setTransform(saveState);
		if (USE_FRAME_STATS) {
			this.currentFrame.drawEnd = System.nanoTime();
		}
	}
	
	private void run() {
		this.engineState = EnumEngineState.RUNNING;
		double totalElapsed = 0;
		final int numberTimesRun = 120;
		int updatesTillInit = numberTimesRun + 21;
		final double _30hz = 1000.0 / 30;
		final double _60hz = 1000.0 / 60;
		final double _120hz = 1000.0 / 120;
		final double _240hz = 1000.0 / 240;
		final double TIME_PER_FRAME = _60hz;
		
		long timer = System.currentTimeMillis();
		long start = System.currentTimeMillis();
		
		while (this.engineState == EnumEngineState.RUNNING) {
			start = System.currentTimeMillis();
			if (USE_FRAME_STATS) {
				this.currentFrame = new FrameStats();
				this.currentFrame.tickCounter = tickCount;
				this.currentFrame.fullLoopStart += System.nanoTime();
				this.tickCount++;
			}
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
						this.framesTillUpdate = 1;
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
					if (MathHelper.equal(averageElapsed, _30hz, 0.05)) {
						this.frameRateType = EnumLockedFrameRate.YES_30;
					} else if (MathHelper.equal(averageElapsed, _60hz, 0.05)) {
						this.frameRateType = EnumLockedFrameRate.YES_60;
					} else if (MathHelper.equal(averageElapsed, _120hz, 0.05)) {
						this.frameRateType = EnumLockedFrameRate.YES_120;
					} else if (MathHelper.equal(averageElapsed, _240hz, 0.05)) {
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
					sleepTime = (long) (TIME_PER_FRAME - elapsed);
					this.updatesTillNonRound = 2;
				} else {
					sleepTime = Math.round(TIME_PER_FRAME - elapsed);
					this.updatesTillNonRound--;
				}

				if (sleepTime >= 0) {
					try {
						if (System.getProperty("os.name").toLowerCase().contains("mac")) {
							sleepTime -= 1;
						}
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
			
			if (USE_FRAME_STATS) {
				this.currentFrame.fullLoopEnd += System.nanoTime();
				System.err.println(this.currentFrame);
				this.statsList.add(currentFrame);
			}
		}
		
		if (USE_FRAME_STATS) {
			double totalUpdate = 0;
			double totalDraw = 0;
			double totalLoop = 0;
			
			for (int i = 0; i < statsList.size(); i++) {
				FrameStats s = statsList.get(i);
				totalUpdate += s.updateEnd - s.updateStart;
				totalDraw += s.drawEnd - s.drawStart;
				totalLoop += s.fullLoopEnd - s.fullLoopStart;
			}
			
			System.out.println("\n\n");
			System.out.println("60hz = " + _60hz + "ms");
			System.out.println("Number of ticks: " + statsList.size());
			System.out.printf("Update: |%15.0f / %1d = %15.3fns = %15.3fms\n", totalUpdate, statsList.size(), (totalUpdate / statsList.size()), (totalUpdate / statsList.size()) / 1000000);
			System.out.printf("Draw:   |%15.0f / %1d = %15.3fns = %15.3fms\n", totalDraw,   statsList.size(), (totalDraw   / statsList.size()), (totalDraw   / statsList.size()) / 1000000);
			System.out.printf("Loop:   |%15.0f / %1d = %15.3fns = %15.3fms\n", totalLoop,   statsList.size(), (totalLoop   / statsList.size()), (totalLoop   / statsList.size()) / 1000000);
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