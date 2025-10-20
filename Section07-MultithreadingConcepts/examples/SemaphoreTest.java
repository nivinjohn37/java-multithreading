package examples;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    public static final Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++) {
            Thread thread = new Thread(() ->{
                try {
                    System.out.println(Thread.currentThread().getId() + " waiting to acquire");
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getId() + " acquired");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    semaphore.release();
                    System.out.println(Thread.currentThread().getId() + " release");
                }
            }, "Thread-" + i);
            thread.start();
        }
    }
}
