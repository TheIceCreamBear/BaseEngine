package com.projecttriumph.engine.gamediscover;

import java.io.File;
import java.net.URLClassLoader;

import com.projecttriumph.engine.api.game.Game;
import com.projecttriumph.engine.api.game.IGameController;

public class GameContainer {
	private File location;
	private Game game;
	private Class<? extends IGameController> controllerClass;
	private boolean controllerCreated = false;
	private IGameController controller;
	private URLClassLoader gameClassLoader;
	
	public GameContainer(File location, Game game, Class<? extends IGameController> controllerClass, URLClassLoader gameClassLoader) {
		this.location = location;
		this.game = game;
		this.controllerClass = controllerClass;
		this.gameClassLoader = gameClassLoader;
	}
	
	public Class<? extends IGameController> getControllerClass() {
		return this.controllerClass;
	}
	
	public void instantiateControllerClass() {
		try {
			this.controller = controllerClass.newInstance();
			this.controllerCreated = true;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public URLClassLoader getGameClassLoader() {
		return this.gameClassLoader;
	}
	
	public IGameController getController() {
		if (!controllerCreated) {
			return null;
		}
		return this.controller;
	}
	
	public Game getGame() {
		return this.game;
	}
	
	public File getLocation() {
		return this.location;
	}
}