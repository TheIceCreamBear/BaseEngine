package com.projecttriumph.engine;

import javax.swing.JOptionPane;

import com.projecttriumph.engine.rendering.ScreenManager;
import com.projecttriumph.engine.threads.ShutdownThread;

/**
 * The Entry point into the GameEngine
 * @author Joseph
 */
public class Main {
	public static void main(String[] args) {
		try {
			if (System.getProperty("sun.arch.data.model").contains("32")) {
				JOptionPane.showMessageDialog(null, "This application does not support the 32 bit JVM, please upgrade to 64bit.\nThe application will now exit.", "JVM Not Supported", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
			
			Runtime runtime = Runtime.getRuntime();
			runtime.addShutdownHook(new ShutdownThread());
			
			
			
			ScreenManager manager = new ScreenManager();
			GameEngine engine = new GameEngine(manager);
			
			// TODO this is where the selection of the game would take place
			
			engine.startEngine();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO create crash report system
			System.exit(-1);
		}
	}
}