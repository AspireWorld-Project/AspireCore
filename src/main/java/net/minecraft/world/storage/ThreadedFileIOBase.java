package net.minecraft.world.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadedFileIOBase implements Runnable {
	public static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();
	private List threadedIOQueue = Collections.synchronizedList(new ArrayList());
	private volatile long writeQueuedCounter;
	private volatile long savedIOCounter;
	private volatile boolean isThreadWaiting;
	private static final String __OBFID = "CL_00000605";

	private ThreadedFileIOBase() {
		Thread thread = new Thread(this, "File IO Thread");
		thread.setPriority(1);
		thread.start();
	}

	@Override
	public void run() {
		while (true) {
			processQueue();
		}
	}

	private void processQueue() {
		for (int i = 0; i < threadedIOQueue.size(); ++i) {
			IThreadedFileIO ithreadedfileio = (IThreadedFileIO) threadedIOQueue.get(i);
			boolean flag = ithreadedfileio.writeNextIO();

			if (!flag) {
				threadedIOQueue.remove(i--);
				++savedIOCounter;
			}

			// try
			// {
			// Thread.sleep(this.isThreadWaiting ? 0L : 10L);
			// }
			// catch (InterruptedException interruptedexception1)
			// {
			// interruptedexception1.printStackTrace();
			// }
		}

		if (threadedIOQueue.isEmpty()) {
			try {
				Thread.sleep(25L);
			} catch (InterruptedException interruptedexception) {
				interruptedexception.printStackTrace();
			}
		}
	}

	public void queueIO(IThreadedFileIO p_75735_1_) {
		if (!threadedIOQueue.contains(p_75735_1_)) {
			++writeQueuedCounter;
			threadedIOQueue.add(p_75735_1_);
		}
	}

	public void waitForFinish() throws InterruptedException {
		isThreadWaiting = true;

		while (writeQueuedCounter != savedIOCounter) {
			Thread.sleep(10L);
		}

		isThreadWaiting = false;
	}

	public void waitForFinish(IThreadedFileIO special) throws InterruptedException {
		while (threadedIOQueue.contains(special)) {
			Thread.sleep(10L);
		}
	}
}
