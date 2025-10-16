package examples;

import java.util.concurrent.locks.ReentrantLock;

public class TryLockExample {

    private final ReentrantLock lock = new ReentrantLock();

    public void performLockedTask() {
        if (lock.tryLock()) { // Non-blocking attempt to acquire the lock
            try {
                System.out.println(Thread.currentThread().getName() + " acquired the lock and is performing the main task.");
                // Simulate some work in the critical section
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " released the lock.");
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " could not acquire the lock. Performing fallback task.");
            performFallbackTask();
        }
    }

    private void performFallbackTask() {
        try {
            // Simulate some less critical, non-locked work
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName() + " completed the fallback task.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        final TryLockExample resource = new TryLockExample();

        Runnable task = () -> {
            for (int i = 0; i < 3; i++) {
                resource.performLockedTask();
                try {
                    // Give other threads a chance to run
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();
    }
}
