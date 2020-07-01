package com.projecttriumph.engine.api.math;

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
}