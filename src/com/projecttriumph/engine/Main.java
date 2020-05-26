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
	private static String gname = "NO_GAME";
	
	public static void main(String[] args) {
		try {
			if (System.getProperty("sun.arch.data.model").contains("32")) {
				JOptionPane.showMessageDialog(null, "This application does not support the 32 bit JVM, please upgrade to 64bit.\nThe application will now exit.", "JVM Not Supported", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
			
			Runtime runtime = Runtime.getRuntime();
			runtime.addShutdownHook(new ShutdownThread());
			
			GameContainer game = null;
			
			// TODO create a better command line args parser
			if (args.length > 0) {
				if (args[0].startsWith("--cp=")) {
					// Load via directory, used for development.
					GameDiscoverer disc = new GameDiscoverer();
					disc.addDirectoryCandidate(args[0].substring(5));
					List<GameCandidate> candidates = disc.identifyValid();
					if (candidates != null && candidates.size() == 1) {
						game = GameLoader.loadGame(candidates.get(0));
					}
				} else if (args[0].startsWith("--jf=")) {
					// Load via jar file, used for all in one distros (engine packaged with the game)
					
				}
			} else {				
				GameDiscoverer disc = new GameDiscoverer();
				disc.findJarGamesInDir(new File("F:\\__TestGameDir")); // TODO make a default location
				List<GameCandidate> candidates = disc.identifyValid();
				GameCandidate selection = GameLoaderDialog.show(candidates);
				game = GameLoader.loadGame(selection);
			}
			
			if (game != null) {
				gname = game.getGame().gameID();
			}
			
			ScreenManager manager = new ScreenManager(game);
			GameEngine engine = new GameEngine(manager, game);
			
			engine.startEngine();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO create crash report system
			System.exit(-1);
		}
	}
	
	public static String getGname() {
		return gname;
	}
}