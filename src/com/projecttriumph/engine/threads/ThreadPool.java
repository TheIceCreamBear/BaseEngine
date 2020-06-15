package com.projecttriumph.engine.threads;

import java.util.LinkedList;

/**
 * A thread pool used to run {@link Runnable} in their own independent thread.
 * @author Joseph
 *
 */
public class ThreadPool extends ThreadGroup {
	private static int poolID;
			
	private boolean alive;
	private LinkedList<Runnable> tasks;
	private WorkerThread[] threads;
	
	public ThreadPool(int numberThreads) {
		super("Thread Pool " + (poolID++));
		this.alive = true;
		this.tasks = new LinkedList<Runnable>();
		
		this.threads = new WorkerThread[numberThreads];
		for (int i = 0; i < threads.length; i++) {
			this.threads[i] = new WorkerThread(this, i);
			this.threads[i].start();
		}
	}
	
	/**
	 * Adds a task to the queue
	 * @param task - the task
	 */
	public void addTask(Runnable task) {
		if (!alive) {
			return;
		}
		
		if (task != null) {
			tasks.add(task);
			synchronized (this) {
				this.notify();
			}
		}
	}
	
	/**
	 * Gets the next task in the queue. if the queue is empty, the method will
	 * loop and wait() until it recieves a notify() call
	 * @return - the next runnable
	 * @throws InterruptedException - if the thread calling this gets interrupted
	 */
	private Runnable getNextTask() throws InterruptedException {
		while (tasks.isEmpty()) {
			if (!alive) {
				return null;
			}
			synchronized (this) {
				this.wait();
			}
		}
		
		return tasks.remove();
	}
	
	/**
	 * Closes this thread pool. All waiting tasks are canceled. All threads 
	 * are stopped. Once closed, no more tasks can be run.
	 */
	public synchronized void close() {
        if (alive) {
            alive = false;
            tasks.clear();
            interrupt();
        }
    }
	
	/**
	 * Joins all threads into the caller thread. Essentially shuts down the 
	 * thread pool. Notifies all threads to run the final task and then 
	 * loops them and calls {@link Thread#join()} on them.
	 */
	public void join() {
		synchronized (this) {
			alive = false;
			this.notifyAll();
		}
		
		Thread[] threads = new Thread[this.activeCount()];
		this.enumerate(threads);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}
	
	/**
	 * Called when a worker thread starts. Does nothing by default, implemented by subclasses.
	 */
	protected void threadStarted() {
		// to be implemented by subclasses
	}
	
	/**
	 * Called when a worker thread stops. Does nothing by default, implemented by subclasses.
	 */
	protected void threadStopped() {
		// to be implemented by subclasses
	}
	
	/**
	 * The threads in this pool. Will run tasks until it is interrupted 
	 * or until {@link ThreadPool#join()} is called
	 * @author Joseph
	 *
	 */
	private class WorkerThread extends Thread {
		public WorkerThread(ThreadPool pool, int id) {
			super(pool, "Worker Thread #" + id);
		}
		
		@Override
		public void run() {
			threadStarted();
			
			while (!isInterrupted()) {
				Runnable task = null;
				
				try {
					task = getNextTask();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (task == null) {
					return;
				}
				
				task.run();
			}
			
			threadStopped();
		}
	}
}