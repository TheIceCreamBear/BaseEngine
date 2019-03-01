package com.projecttriumph.engine.rendering;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.projecttriumph.engine.api.io.user.IGameMouseInputHandler;
import com.projecttriumph.engine.api.math.shape.Rectangle;
import com.projecttriumph.engine.io.user.EngineMouseInputHandler;
import com.projecttriumph.engine.io.user.KeyInputHandler;

public class ScreenManager {
	private GraphicsDevice device;
	private final JFrame frame;
	private final Camera camera;
	
	private static ScreenManager instance;
	
	public ScreenManager() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.device = env.getDefaultScreenDevice();
		
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setUndecorated(true);
		this.frame.setIgnoreRepaint(true);
		this.frame.setResizable(false);
		
		this.device.setFullScreenWindow(frame);
		this.device.setDisplayMode(device.getDisplayMode());
		
		// TODO make the camera be set somewhere else
		this.camera = new Camera(getScreenWidth(), getScreenHeight());
		
		this.frame.createBufferStrategy(2);
		
		instance = this;
	}
	
	public Graphics2D getRenderGraphics() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			BufferStrategy strategy = window.getBufferStrategy();
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
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
		this.frame.addMouseWheelListener(gmih);
	}
	
	public void removeGameMouseListener(IGameMouseInputHandler gmih) {
		// TODO safety check
		this.frame.removeMouseListener(gmih);
		this.frame.removeMouseWheelListener(gmih);
	}
	
	public void addInputListeners(KeyInputHandler kih, EngineMouseInputHandler emih) {
		this.frame.addKeyListener(kih);
		this.frame.addMouseListener(emih);
		this.frame.addMouseWheelListener(emih);
	}
	
	public int getScreenWidth() {
		return device.getDisplayMode().getWidth();
	}
	
	public int getScreenHeight() {
		return device.getDisplayMode().getHeight();
	}
	
	public int getRefreshRate() {
		return device.getDisplayMode().getRefreshRate();
	}
	
	public Rectangle getScreenBounds() {
		return new Rectangle(0, 0, getScreenWidth(), getScreenHeight());
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public static ScreenManager getInstance() {
		return instance;
	}
}