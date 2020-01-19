package com.projecttriumph.engine.gamediscover;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.objectweb.asm.Type;

import com.projecttriumph.engine.api.game.Game;
import com.projecttriumph.engine.api.game.IGameController;

public class GameLoader {	
	public static GameContainer loadGame(GameCandidate candidate) {
		Set<String> classes = candidate.getClasses();
		try {
			URLClassLoader cl = new URLClassLoader(new URL[] {candidate.getLocation().toURI().toURL()}, GameContainer.class.getClassLoader());
			for (String string : classes) {
				try {
					String name = Type.getObjectType(string).getClassName();
					@SuppressWarnings("unused")
					// loads class
					Class<?> clazz = Class.forName(name, true, cl);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			Class<?> gameClass = Class.forName(candidate.getGameClassName(), false, cl);
			Game game = gameClass.getAnnotation(Game.class);
			Class<? extends IGameController> controllerClass = gameClass.asSubclass(IGameController.class);
			IGameController controller = controllerClass.newInstance();
			return new GameContainer(candidate.getLocation(), game, controller);
		} catch (ClassNotFoundException | ClassCastException | InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}