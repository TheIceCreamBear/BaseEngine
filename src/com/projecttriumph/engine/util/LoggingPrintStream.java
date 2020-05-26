package com.projecttriumph.engine.util;

import java.io.PrintStream;

import org.apache.logging.log4j.Logger;

/**
 * A print stream to take a given print stream and forward its output to a given
 * logger
 * 
 * @author Joseph
 */
public class LoggingPrintStream extends PrintStream {
	private Logger logger;
	private int BASE_DEPTH = 3;
	
	public LoggingPrintStream(Logger logger, PrintStream original) {
		super(original);
		this.logger = logger;
	}
	
	@Override
	public void println(String s) {
		logger.info("{}{}", getPrefix(), s);
	}
	
	@Override
	public void println(Object o) {
		logger.info("{}{}", getPrefix(), o);
	}
	
	private String getPrefix() {
		StackTraceElement[] elems = Thread.currentThread().getStackTrace();
		StackTraceElement elem = elems[BASE_DEPTH]; // The caller is always at BASE_DEPTH, including this call.
		if (elem.getClassName().startsWith("java.lang.Throwable")) {
			elem = elems[BASE_DEPTH + 4];
		}
		return "[" + elem.getClassName() + ":" + elem.getMethodName() + ":" + elem.getLineNumber() + "]: ";
	}
}
