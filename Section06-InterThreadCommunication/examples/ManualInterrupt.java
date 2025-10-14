package examples;

public class ManualInterrupt {

    public static void main(String[] args) {
        // Create and start the three threads
        Thread t1 = new Thread(() -> {
            System.out.println("t1 running...");
            try {
                // Simulate some work
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("t1 was interrupted!");
            }
        });

        Thread t2 = new Thread(() -> System.out.println("t2 running..."));
        Thread t3 = new Thread(() -> System.out.println("t3 running..."));

        t1.start();
        t2.start();
        t3.start();

        // Start a new thread to interrupt the main thread
        // after a short delay.
        Thread interruptor = new Thread(() -> {
            try {
                Thread.sleep(100); // Wait 100ms before interrupting
                System.out.println("Interrupting main thread...");
                Thread.currentThread().interrupt(); // Interrupt this thread
            } catch (InterruptedException e) {
                System.out.println("The interruptor thread was interrupted!");
            }
        });

        // The main thread is the one that calls `join()`, so that's the one we
        // want to interrupt.
        Thread mainThread = Thread.currentThread();

        // This separate thread will target the main thread for interruption
        Thread killerThread = new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("Killer thread interrupting main thread...");
                mainThread.interrupt(); // Interrupt the main thread
            } catch (InterruptedException e) {
                // This won't be called in this example
            }
        });
        killerThread.start();

        System.out.println("Main thread is joining t1, t2, and t3...");

        try {
            t1.join(); // Main thread waits here. It will be interrupted.
            t2.join(); // This might not be reached depending on the timing
            t3.join(); // This might not be reached depending on the timing
        } catch (InterruptedException e) {
            System.out.println("Main thread caught an InterruptedException!");
            e.printStackTrace();
        }

        System.out.println("Main thread finished.");
    }
}
