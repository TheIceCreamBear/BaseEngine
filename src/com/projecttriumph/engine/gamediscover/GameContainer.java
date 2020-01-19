package com.projecttriumph.engine.gamediscover;

import java.io.File;

import com.projecttriumph.engine.api.game.Game;
import com.projecttriumph.engine.api.game.IGameController;

public class GameContainer {
	private File location;
	private Game game;
	private IGameController controller;
	
	public GameContainer(File location, Game game, IGameController controller) {
		this.location = location;
		this.game = game;
		this.controller = controller;
	}
	
	public IGameController getController() {
		return this.controller;
	}
	
	public Game getGame() {
		return this.game;
	}
	
	public File getLocation() {
		return this.location;
	}
}