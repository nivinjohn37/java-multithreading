package SelfAssesment.Practice;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeCounter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }

    public static void main(String[] args) {
        ThreadSafeCounter counter = new ThreadSafeCounter();

        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                counter.increment();
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assert counter.getCount() == 20;
        System.out.println("Final Count -> " + counter.getCount());

    }
}
