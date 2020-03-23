package com.projecttriumph.engine.rendering;

public class FrameStats {
	public long updateStart;
	public long updateEnd;
	public long drawStart;
	public long drawEnd;
	public long fullLoopStart;
	public long fullLoopEnd;
	public long tickCounter;
	
	@Override
	public String toString() {
		return "Tick: " + tickCounter + " Update: " + (updateEnd - updateStart) + "ns Draw: " + (drawEnd - drawStart) + "ns FullLoop: " + (fullLoopEnd - fullLoopStart) + "ns";
	}
}
