package examples;

public class Application {
    public static void main(String[] args) {
        LoggingService logService = new LoggingService();

        // Create and start multiple threads
        Thread thread1 = new Thread(() -> {
            logService.logMessage("Task 1 started");
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            logService.logMessage("Task 1 finished");
        });

        Thread thread2 = new Thread(() -> {
            logService.logMessage("Task 2 started");
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            logService.logMessage("Task 2 finished");
        });

        thread1.start();
        thread2.start();

        // Ensure the main thread waits for logging tasks to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logService.shutdown();
    }
}
