package com.projecttriumph.engine.io.file;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.net.URLClassLoader;

import javax.imageio.ImageIO;

public class ImageHelper {
	private static boolean initialized = false;
	private static GraphicsConfiguration graphicsConfig; 
	private static URLClassLoader classLoader;
	// TODO add more methods
	
	public static void init(GraphicsConfiguration gc, URLClassLoader gameClassLoader) {
		if (initialized) {
			return;
		}
		
		graphicsConfig = gc;
		classLoader = gameClassLoader;
		initialized = true;
	}
	
	// documentation note, warn users not to call this inside of a constructor, only inside of the initialize method
	// of the game controller. If called in another place, this will return null as this will not be initialized
	// documentation note, warn users to not put a / infront of their location string, as that might not get their file properly
	public static BufferedImage readImageFromClassPath(String location) throws Exception {
		if (!initialized) {
			return null;
		}
		
		BufferedImage im = ImageIO.read(classLoader.getResource(location));
		BufferedImage converted = graphicsConfig.createCompatibleImage(im.getWidth(), im.getHeight(), im.getTransparency());
		
		Graphics2D g = converted.createGraphics();
		g.drawImage(im, 0, 0, im.getWidth(), im.getHeight(), null);
		g.dispose();
		
		return converted;
	}
}
