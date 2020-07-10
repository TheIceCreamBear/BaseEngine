package com.projecttriumph.engine.io.file;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageHelper {
	private static boolean initialized = false;
	private static GraphicsConfiguration graphicsConfig; 
	// TODO add more methods
	
	public static void init(GraphicsConfiguration gc) {
		if (initialized) {
			return;
		}
		
		graphicsConfig = gc;
		initialized = true;
	}
	
	public static BufferedImage readImageFromClassPath(String location) throws Exception {
		if (!initialized) {
			return null;
		}
		
		BufferedImage im = ImageIO.read(ImageHelper.class.getResource(location));
		BufferedImage converted = graphicsConfig.createCompatibleImage(im.getWidth(), im.getHeight(), im.getTransparency());
		
		Graphics2D g = converted.createGraphics();
		g.drawImage(im, 0, 0, im.getWidth(), im.getHeight(), null);
		g.dispose();
		
		return converted;
	}
}
