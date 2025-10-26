package SelfAssesment.Practice;

public class ReentrantLock {
    private boolean locked = false;
    private int lockCount = 0;
    private Thread lockedThread;

    public synchronized void lock() throws InterruptedException {
        while(locked && Thread.currentThread() != lockedThread) {
            wait();
        }

        locked = true;
        lockCount++;
        lockedThread = Thread.currentThread();
    }
}
