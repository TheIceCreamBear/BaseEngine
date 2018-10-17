package com.projecttriumph.engine.api.math;

/**
 * A class representing a 2D vector. Can be used to represent movement or acceleration of an object.
 * @author Joseph
 */
public class Vector {
	private double magnitude;
	private double angle;
	private double i;
	private double j;
	
	/**
	 * Constructs a new Vector object with the given magnitude and angle (in radians).
	 * Will calculate the x and y components based on the givens.
	 * 
	 * @param magnitude - the magnitude of the vector
	 * @param angle - the angle of the vector in radians.
	 */
	public Vector(double magnitude, double angle) {
		this.magnitude =  magnitude;
		this.angle = angle;
		this.i = this.magnitude * Math.cos(this.angle);
		this.j = this.magnitude * Math.sin(this.angle);
	}
	
	/**
	 * private adding constructor. adds the two together.
	 * 
	 * @param v1
	 * @param v2
	 */
	private Vector(Vector v1, Vector v2) {
		this.i = v1.i + v2.i;
		this.j = v1.j + v2.j;
		this.magnitude = Math.sqrt(Math.pow(this.i, 2) + Math.pow(this.j, 2));
		this.angle = Math.atan(this.j / this.i);
	}
	
	public double getI() {
		return this.i;
	}
	
	public double getJ() {
		return this.j;
	}
	
	public double getAngle() {
		return this.angle;
	}
	
	public double getMagnitude() {
		return this.magnitude;
	}
	
	/**
	 * Calculates and return the dot product of this vector and the given vector.
	 * 
	 * @param other - the other vector to use in the calculations
	 * @return - the dot product of the two vector
	 */
	public double dotProduct(Vector other) {
		return i * other.i + j * other.j;
	}
	
	/**
	 * Creates and returns a new vector equal to this vector plus the given vector.
	 * Use this method for adding an acceleration vector to a movement vector.
	 * 
	 * @param other - the vector to add to this one
	 * @return - a new vector 
	 */
	public Vector add(Vector other) {
		return new Vector(this, other);
	}
	
	/**
	 * Creates and returns a new vector equal to this vector plus the given vector * -1.
	 * 
	 * @param other - the vector to subtract from this one to this one
	 * @return - a new vector 
	 */
	public Vector subtract(Vector other) {
		return new Vector(this, other.multiply(-1));
	}
	
	/**
	 * Creates a new vector with the same angle and the magnitude of this vector * a given scalar value.
	 * 
	 * @param scalar - a scalar value
	 * @return - a new vector
	 */
	public Vector multiply(double scalar) {
		return new Vector(magnitude * scalar, angle);
	}
	
	@Override
	public String toString() {
		return super.toString() + " i:" + this.i + " j:" + this.j + " mag:" + this.magnitude + " ang:" + this.angle;
	}
}