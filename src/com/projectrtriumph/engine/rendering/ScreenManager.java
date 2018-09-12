package com.projectrtriumph.engine.rendering;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.projectrtriumph.engine.io.user.KeyInputHandler;
import com.projectrtriumph.engine.io.user.MouseInputHandler;

public class ScreenManager {
	private GraphicsDevice device;
	private final JFrame frame;
	
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
		
		this.frame.createBufferStrategy(2);
		
		instance = this;
	}
	
	public Graphics2D getRenderGraphics() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			BufferStrategy strategy = window.getBufferStrategy();
			return (Graphics2D) strategy.getDrawGraphics();
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
	
	public void addInputListeners(KeyInputHandler kih, MouseInputHandler mih) {
		this.frame.addKeyListener(kih);
		this.frame.addMouseListener(mih);
		this.frame.addMouseWheelListener(mih);
	}
	
	public static ScreenManager getInstance() {
		return instance;
	}
}