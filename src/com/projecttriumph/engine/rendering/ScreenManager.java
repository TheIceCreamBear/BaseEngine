package com.projecttriumph.engine.rendering;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.projecttriumph.engine.api.io.user.IGameKeyInputHandler;
import com.projecttriumph.engine.api.io.user.IGameMouseInputHandler;
import com.projecttriumph.engine.api.math.shape.Rectangle;
import com.projecttriumph.engine.gamediscover.GameContainer;
import com.projecttriumph.engine.io.user.EngineMouseInputHandler;
import com.projecttriumph.engine.io.user.KeyInputHandler;

public class ScreenManager {
	private GraphicsDevice device;
	private final JFrame frame;
	private final Camera camera;
	private final FontRenderContext frc;
	
	private static ScreenManager instance;
	
	public ScreenManager(GameContainer game) throws InstantiationException, IllegalAccessException {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.device = env.getDefaultScreenDevice();
		
		this.frame = new JFrame(game.getGame().gameName());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setUndecorated(true);
		this.frame.setIgnoreRepaint(true);
		this.frame.setResizable(false);
		
		this.device.setFullScreenWindow(frame);
		this.device.setDisplayMode(device.getDisplayMode());
		
		this.frc = ((Graphics2D) this.frame.getGraphics()).getFontRenderContext();
		
		if (game.getGame().camera() == Camera.class) {
			this.camera = new Camera(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());
		} else {
			Class<? extends Camera> cameraClass = game.getGame().camera();
			this.camera = cameraClass.newInstance();
		}
		
		this.frame.createBufferStrategy(2);
		
		instance = this;
	}
	
	public Graphics2D getRenderGraphics() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			BufferStrategy strategy = window.getBufferStrategy();
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			return g;
		}
		return null;
	}
	
	public void updateDisplay() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			BufferStrategy strategy = window.getBufferStrategy();
			if (!strategy.contentsLost()) {
				strategy.show();
			}
		}
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void addGameMouseListener(IGameMouseInputHandler gmih) {
		this.frame.addMouseListener(gmih);
		this.frame.addMouseMotionListener(gmih);
		this.frame.addMouseWheelListener(gmih);
	}
	
	public void removeGameMouseListener(IGameMouseInputHandler gmih) {
		// TODO safety check
		this.frame.removeMouseListener(gmih);
		this.frame.removeMouseMotionListener(gmih);
		this.frame.removeMouseWheelListener(gmih);
	}
	
	public void addGameKeyListener(IGameKeyInputHandler gkih) {
		this.frame.addKeyListener(gkih);
	}
	
	public void removeGameKeyListener(IGameKeyInputHandler gkih) {
		// TODO safety check
		this.frame.removeKeyListener(gkih);
	}
	
	public void addInputListeners(KeyInputHandler kih, EngineMouseInputHandler emih) {
		this.frame.addKeyListener(kih);
		this.frame.addMouseListener(emih);
		this.frame.addMouseMotionListener(emih);
		this.frame.addMouseWheelListener(emih);
	}
	
	public void setCursor(Cursor c) {
		this.frame.setCursor(c);
	}
	
	public Point getMousePoint() {
		return this.frame.getContentPane().getMousePosition();
	}
	
	public Rectangle getScreenBounds() {
		return new Rectangle(0, 0, getScreenWidth(), getScreenHeight());
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public static FontRenderContext getFrc() {
		return getInstance().frc;
	}
	
	public static int getScreenWidth() {
		return getInstance().device.getDisplayMode().getWidth();
	}
	
	public static int getScreenHeight() {
		return getInstance().device.getDisplayMode().getHeight();
	}
	
	public static int getRefreshRate() {
		return getInstance().device.getDisplayMode().getRefreshRate();
	}
	
	public static ScreenManager getInstance() {
		return instance;
	}
}