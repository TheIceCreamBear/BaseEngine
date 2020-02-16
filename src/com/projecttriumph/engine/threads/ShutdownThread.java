package com.projecttriumph.engine.threads;

import java.util.ArrayList;

public class ShutdownThread extends Thread {
	private static ShutdownThread instance;
	private ArrayList<Runnable> hooks;
	
	public ShutdownThread() {
		super("Shutdown Hook");
		instance = this;

		hooks = new ArrayList<Runnable>();
	}
	
	public static ShutdownThread getInstance() {
		return instance;
	}
	
	/**
	 * Adds the specified hook to a maintained list of hooks. Use weight to determine the rough
	 * positioning and timing of the specified hook.
	 * @param r - the hook to add.
	 * @param weight - used to determine the position in the list. Higher number, run later
	 */
	public void addHook(Runnable r, int weight) {
		if (this.hooks.get(weight) == null) {
			this.hooks.add(weight, r);
			return;
		}
		
		for (int i = weight + 1; i < this.hooks.size(); i++) {
			if (this.hooks.get(i) == null) {
				this.hooks.add(i, r);
				return;
			}
		}
		
		this.hooks.add(r);
	}

	@Override
	public void run() {
		for (int i = 0; i < hooks.size(); i++) {
			hooks.get(i).run();
		}
	}
}