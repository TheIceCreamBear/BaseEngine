package com.projecttriumph.engine.api.sound;

/**
 * Container for sound samples. Stored as a byte array.
 * Also contains its ID.
 * @author Joseph
 *
 */
public class Sound {
	private String id;
	private byte[] samples;
	
	/**
	 * Construct a new sound
	 * @param id
	 * @param samples
	 */
	public Sound(String id, byte[] samples) {
		this.id = id;
		this.samples = samples;
	}
	
	public String getId() {
		return this.id;
	}
	
	public byte[] getSamples() {
		return this.samples;
	}
}
