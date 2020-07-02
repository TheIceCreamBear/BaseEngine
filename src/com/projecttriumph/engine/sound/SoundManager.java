package com.projecttriumph.engine.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.projecttriumph.engine.api.sound.Sound;
import com.projecttriumph.engine.threads.ThreadPool;

/**
 * A global sound manager for the engine system. Supports custom sound loading and running multiple 
 * sounds at a time. TODO Currently only supports Microsoft wav files
 * @author Joseph
 */
public class SoundManager extends ThreadPool {
	private static HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
	private static SoundManager instance;
	
	private AudioFormat playbackFormat;
	private ThreadLocal<SourceDataLine> localLine;
	private ThreadLocal<byte[]> localBuffer;
	private Object pausedLock;
	private boolean paused;
	
	/**
	 * Creates a new sound manager using {@link SoundManager#SoundManager(AudioFormat, int)}, and 
	 * passes in the maximum amout of sounds that can be played at onces.
	 * <p>
	 * <strong><em>NOTE:</em></strong> DO NOT CALL THIS METHOD IF YOU ARE NOT THE ENGINE OR YOU DO 
	 * NOT KNOW WHAT YOU ARE DOING!!!!
	 * @param playbackFormat - the audio format to use
	 */
	public SoundManager(AudioFormat playbackFormat) {
		this(playbackFormat, getMaxSimultaneousSounds(playbackFormat));
	}
	
	/**
	 * Creates a new sound manager. Initializes the paused lock object and the parent thread pool.
	 * Takes the minimum of the specified max that is passed in and the maximum number of sounds.
	 * <p>
	 * <strong><em>NOTE:</em></strong> DO NOT CALL THIS METHOD IF YOU ARE NOT THE ENGINE OR YOU DO 
	 * NOT KNOW WHAT YOU ARE DOING!!!!
	 * @param playbackFormat - the audio format to use
	 * @param maxSounds - user specified maximum number of sounds. will result in a value that is
	 * greater than one or less than the maximum number of sounds the specified audio format supports
	 */
	public SoundManager(AudioFormat playbackFormat, int maxSounds) {
		super(Math.min(Math.max(maxSounds, 1), getMaxSimultaneousSounds(playbackFormat)));
		this.playbackFormat = playbackFormat;
		this.localLine = new ThreadLocal<SourceDataLine>();
		this.localBuffer = new ThreadLocal<byte[]>();
		this.pausedLock = new Object();
		
		
		synchronized (this) {
			notifyAll();
		}
		
		instance = this;
	}
	
	/**
	 * Gets the instance of this class. If there is none, it will NOT create a new one.
	 * @return the saved instance of SoundManager
	 */
	public static SoundManager getInstance() {
		return instance;
	}
	
	private static int getMaxSimultaneousSounds(AudioFormat playbackFormat) {
		DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, playbackFormat);
		int retval = AudioSystem.getMixer(null).getMaxLines(lineInfo);
		if (retval == AudioSystem.NOT_SPECIFIED) {
			retval = 100;
		}
		return retval;
	}
	
	/**
	 * Loads a new sound by loading the specified resource from the class path. Most usefull when
	 * loading a file that is inside of a jar file or on the class path by some other means.
	 * <p>
	 * It will register the given sound to the system. And if the id already exists, the currently stored
	 * sound will be overwritten.
	 * 
	 * @param resource - the resource to load
	 * @param id - the id to save the sound under
	 * @return - the new sound generated by this method
	 * @throws IOException if the resource fails to load or a reading error occurs
	 * @see SoundManager#getSoundFromInputStream(InputStream, String)
	 */
	public static Sound getSoundFromClassPathFile(String resource, String id) throws IOException  {
		return getSoundFromInputStream(SoundManager.class.getResourceAsStream(resource), id);
	}
	
	/**
	 * Loads a new sound from the file system specified by location string and registers it with id.
	 * <p>
	 * It will register the given sound to the system. And if the id already exists, the currently stored
	 * sound will be overwritten.
	 * 
	 * @param location - the path (absolute or relative) to the file trying to be loaded
	 * @param id - the id to save the sound under
	 * @return the new sound generated by this method
	 * @throws IOException if the resource fails to load or a reading error occurs
	 * @see SoundManager#getSoundFromInputStream(InputStream, String)
	 */
	public static Sound getSoundFromSystemFile(String location, String id) throws IOException {
		return getSoundFromSystemFile(new File(location), id);
	}
	
	/**
	 * Loads a new sound from the file system specified by a reference to the file and registers it with id.
	 * <p>
	 * It will register the given sound to the system. And if the id already exists, the currently stored
	 * sound will be overwritten.
	 * 
	 * @param f - the file trying to be loaded
	 * @param id - the id to save the sound under
	 * @return the new sound generated by this method
	 * @throws IOException if the resource fails to load or a reading error occurs
	 * @see SoundManager#getSoundFromInputStream(InputStream, String)
	 */
	public static Sound getSoundFromSystemFile(File f, String id) throws IOException {
		return getSoundFromInputStream(new FileInputStream(f), id);
	}
	
	/**
	 * Loads a new sound from the specified input stream and registers it with id. This is the final stop
	 * before the sound actually begins loading and is provided as public for convince.
	 * <p>
	 * It will register the given sound to the system. And if the id already exists, the currently stored
	 * sound will be overwritten.
	 * @param is - the input stream to load a sound from
	 * @param id - the id to save the sound under
	 * @return the new sound generated by this method
	 * @throws IOException if the resource fails to load or a reading error occurs
	 */
	public static Sound getSoundFromInputStream(InputStream is, String id) throws IOException {
		return instance.getSound(is, id);
	}
	
	private Sound getSound(InputStream is, String id) throws IOException {
		if (!is.markSupported()) {
			is = new BufferedInputStream(is);
		}
		
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(playbackFormat, AudioSystem.getAudioInputStream(is));
		} catch (UnsupportedAudioFileException e) {
			System.err.println("An error occured while loading the sound with id " + id + " in.");
			System.err.println(e.getLocalizedMessage());
			throw new IOException(e);
		}

		if (ais == null) {
			return null;
		}
		
		int length = (int) (ais.getFrameLength() * ais.getFormat().getFrameSize());

		byte[] samples = new byte[length];
		
		try (DataInputStream dis = new DataInputStream(ais)) {
			dis.readFully(samples);
		} catch (IOException e) {
			System.err.println("An error occured while reading the sound with id " + id + " in.");
			throw e;
		}
		
		Sound s = new Sound(id, samples);
		soundMap.put(id, s);
		return s;
	}
	
	/**
	 * Pauses the sound manager and closes the mixer that is tied to this manager.
	 */
	protected void onClose() {
		
		// close mixer
		Mixer mixer = AudioSystem.getMixer(null);
		if (mixer.isOpen()) {
			mixer.close();
		}
	}
	
	public void close() {
		this.onClose();
		super.close();
	}
	
	public void join() {
		this.onClose();
		super.join();
	}
	
	/**
	 * Sets the desired pause state. Sounds may not pause immediately.
	 * @param paused - new pause state
	 */
	public void setPaused(boolean paused) {
		if (this.paused != paused) {
			synchronized (pausedLock) {
				this.paused = paused;
				if (!paused) {
					// restart sounds
					this.pausedLock.notifyAll();
				}
			}
		}
	}
	
	/**
	 * Returns the current pauses state
	 * @return
	 */
	public boolean isPaused() {
		return this.paused;
	}
	
	/**
	 * Plays the sound that correlates to the specified id on the 
	 * global SoundManager instance. This method adds the sound to the queue 
	 * and returns immediately.
	 * @param id - the id of the sound to play
	 */
	public static void play(String id) {
		instance.play(soundMap.get(id));
	}
	
	/**
	 * Plays the specified sound. This method adds the sound to the queue 
	 * and returns immediately.
	 * @param sound - the sound to play
	 */
	public void play(Sound sound) {
		this.play(sound, null, false);
	}
	
	/**
	 * Plays the specified sound, with the specified filter and loops if specified. 
	 * This method adds the sound to the queue and returns immediately.
	 * @param sound - the sound to play
	 * @param filter - TODO make this an actual thing
	 * @param loop - weather or not to loop the input
	 */
	public void play(Sound sound, Object filter, boolean loop) {
		if (sound != null) {
			InputStream is;
			if (loop) {
				is = new LoopingByteArrayInputStream(sound.getSamples());
			} else {
				is = new ByteArrayInputStream(sound.getSamples());
			}
			
			this.play(is, filter);
		}
	}
	
	private void play(InputStream is, Object filter) {
		if (is != null) {
			if (filter != null) {
//				is = new FilteredSoundStream(is, filter);
			}
			
			super.addTask(new SoundPlayer(is));
		}
	}
	
	/**
	 * Signals that a PooledThread has started. Creates the Thread's line and
	 * buffer.
	 */
	protected void threadStarted() {
		// wait for constructor
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException ex) {
			}
		}
		
		// use a short, 100ms (1/10th sec) buffer for filters that
		// change in real-time
		int bufferSize = playbackFormat.getFrameSize() * Math.round(playbackFormat.getSampleRate() / 10);
		
		// create, open, and start the line
		SourceDataLine line;
		DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, playbackFormat);
		try {
			line = (SourceDataLine) AudioSystem.getLine(lineInfo);
			line.open(playbackFormat, bufferSize);
		} catch (LineUnavailableException ex) {
			// the line is unavailable - signal to end this thread
			Thread.currentThread().interrupt();
			ex.printStackTrace();
			return;
		}
		
		line.start();
		
		// create the buffer
		byte[] buffer = new byte[bufferSize];
		
		// set this thread's locals
		localLine.set(line);
		localBuffer.set(buffer);
	}
	
	/**
	 * Signals that a PooledThread has stopped. Drains and closes the Thread's
	 * Line.
	 */
	protected void threadStopped() {
		SourceDataLine line = (SourceDataLine) localLine.get();
		if (line != null) {
			line.drain();
			line.close();
		}
	}
	
	/**
	 * A runnable task responsible for playing a sound from an input stream.
	 * @author Joseph
	 *
	 */
	private class SoundPlayer implements Runnable {
		
		private InputStream source;
		
		public SoundPlayer(InputStream source) {
			this.source = source;
		}
		
		public void run() {
			// get line and buffer from ThreadLocals
			SourceDataLine line = localLine.get();
			byte[] buffer = localBuffer.get();
			if (line == null || buffer == null) {
				// the line is unavailable
				return;
			}
			
			// copy data to the line
			try {
				int numBytesRead = 0;
				while (numBytesRead != -1) {
					// if paused, wait until unpaused
					synchronized (pausedLock) {
						if (paused) {
							try {
								pausedLock.wait();
							} catch (InterruptedException ex) {
								return;
							}
						}
					}
					// copy data
					numBytesRead = source.read(buffer, 0, buffer.length);
					if (numBytesRead != -1) {
						line.write(buffer, 0, numBytesRead);
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
}