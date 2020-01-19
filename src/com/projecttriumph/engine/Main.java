package com.projecttriumph.engine;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import com.projecttriumph.engine.gamediscover.GameCandidate;
import com.projecttriumph.engine.gamediscover.GameContainer;
import com.projecttriumph.engine.gamediscover.GameDiscoverer;
import com.projecttriumph.engine.gamediscover.GameLoader;
import com.projecttriumph.engine.gamediscover.GameLoaderDialog;
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
			
			GameDiscoverer disc = new GameDiscoverer();
			disc.findGamesInDir(new File("F:\\__TestGameDir")); // TODO make a default location
			List<GameCandidate> candidates = disc.identifyValid();
			GameCandidate selection = GameLoaderDialog.show(candidates);
			GameContainer game = GameLoader.loadGame(selection); // TODO use
			
			ScreenManager manager = new ScreenManager(game);
			GameEngine engine = new GameEngine(manager, game);
			
			engine.startEngine();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO create crash report system
			System.exit(-1);
		}
	}
}