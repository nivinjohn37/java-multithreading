package examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class SingleThreadExecutorWithFuture {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?>[] futures = new Future<?>[5];

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            futures[i-1] = executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " → Executing Task " + taskId);
                try {
                    Thread.sleep(1000);
                    if (taskId == 3) { // Force a failure on a specific task
                        throw new RuntimeException("Simulated failure for Task 3");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();

        // Retrieve and handle exceptions from each future
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                System.err.println("Caught an exception from a task: " + e.getCause().getMessage());
            }
        }
    }
}
