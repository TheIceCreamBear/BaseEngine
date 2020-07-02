package com.projecttriumph.engine.api.math;

import java.awt.geom.Point2D;

public class MathHelper {
	/**
	 * Checks to see if the two number are equal within the given error margin
	 * @param a - the first number
	 * @param b - the second number
	 * @param error - the error to check that the difference between the two numbers is less than
	 * @return true if |a - b| <= error
	 */
	public static boolean equal(double a, double b, double error) {
		double ab = Math.abs(a - b);
		return ab <= error;
	}
	
	/**
	 * Finds the angle in radians above the horizontal that the line these two points form from 
	 * the starting object to the target.
	 * @param object - the calling object
	 * @param target - the target object to find the able to
	 * @return the angle in radians from the starting object to the target
	 */
	public static double getAngleRad(Point2D object, Point2D target) {
		return Math.atan2(target.getY() - object.getY(), target.getX() - object.getX());
	}
	
	/**
	 * Finds the angle in degrees above the horizontal that the line these two points form from 
	 * the starting object to the target.
	 * @param object - the calling object
	 * @param target - the target object to find the able to
	 * @return the angle in degrees from the starting object to the target
	 */
	public static double getAngle(Point2D object, Point2D target) {
		return Math.toDegrees(getAngleRad(object, target));
	}
	
	/**
	 * Squares a number
	 * @param a - the number to square
	 * @return a * a
	 */
	public static double square(double a) {
		return a * a;
	}
	
	/**
	 * Calculates the distance squared between the two points.
	 * @param p1 - the first point
	 * @param p2 - the second point
	 * @return the square of the distance between the two points
	 */
	public static double getDistanceSqrd(Point2D p1, Point2D p2) {
		return square(p2.getX() - p1.getX()) + square(p2.getY() + p1.getY());
	}
	
	/**
	 * Calculates the distance between the two points.
	 * @param p1 - the first point
	 * @param p2 - the second point
	 * @return the distance between the two points
	 */
	public static double getDistance(Point2D p1, Point2D p2) {
		return Math.sqrt(getDistanceSqrd(p1, p2));
	}
}