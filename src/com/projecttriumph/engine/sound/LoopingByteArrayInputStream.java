package com.projecttriumph.engine.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class LoopingByteArrayInputStream extends ByteArrayInputStream {
	private boolean closed;
	
	/**
	 * Creates a new looping input stream. The given array is not coppied
	 * @param buf - the byte array to read
	 */
	public LoopingByteArrayInputStream(byte[] buf) {
		super(buf);
		this.closed = false;
	}
	
	public int read(byte[] buffer, int offset, int length) {
        if (closed) {
            return -1;
        }
        int totalBytesRead = 0;

        while (totalBytesRead < length) {
            int numBytesRead = super.read(buffer,
                offset + totalBytesRead,
                length - totalBytesRead);

            if (numBytesRead > 0) {
                totalBytesRead += numBytesRead;
            }
            else {
                reset();
            }
        }
        return totalBytesRead;
    }
	
	/**
	 * Closes the stream. Future calls will return -1
	 */
	public void close() throws IOException {
        super.close();
        this.closed = true;
    }
}
