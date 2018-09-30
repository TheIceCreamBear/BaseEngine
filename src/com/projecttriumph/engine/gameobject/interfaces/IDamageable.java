package com.projecttriumph.engine.gameobject.interfaces;

public interface IDamageable extends IBoundingBox {
	/**
	 * TODO determine how damage will be calculated and passed.
	 * Will there be an enum for damage source? will it just be an int?
	 * Will it take in a IDamageSource Instance?
	 * TBD
	 */
	public void damage();
}