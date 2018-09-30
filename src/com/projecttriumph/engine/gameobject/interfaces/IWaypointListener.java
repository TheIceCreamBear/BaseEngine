package com.projecttriumph.engine.gameobject.interfaces;

import java.awt.geom.Point2D;

import com.projecttriumph.engine.math.Vector;

public interface IWaypointListener {
	/**
	 * add the offset vector for the waypoint listener. Used to move groups of objects 
	 * and keep their arrangement relative to each other. an object should have a list
	 * of waypoints to add this one to.
	 * @param v - the vector they will travel
	 */
	public void addWaypointVector(Vector v);
	
	public void addWaypointPoint(Point2D point);
}