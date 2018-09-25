package com.projecttriumph.engine.math;

public class MathHelper {
	public static boolean equal(double a, double b, double error) {
		double ab = Math.abs(a - b);
		return ab <= error;
	}
}