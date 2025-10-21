package examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelTaskExecutor {
    static final int totalTasks = 50;

    static class Task implements Callable<String> {

        int id;

        public Task(int id) {
            this.id = id;
        }

        @Override
        public String call() throws Exception {
            System.out.println("Executing Task " + id + " on thread: " + Thread.currentThread().getName());
            try {
                // Simulate work with a random delay
                Thread.sleep(500 + (long)(Math.random() * 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "This is a task -> " + id;
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<String>> futures = new ArrayList<>(totalTasks);

        System.out.println("Submitting " + totalTasks + " tasks to the executor...");

        for (int i = 1; i <= totalTasks; i++) {
            futures.add(executor.submit(new Task(i))); // Corrected line
        }
        
        System.out.println("All tasks submitted. Waiting for completion...");

        try {
            for (Future<String> future : futures) {
                System.out.println(future.get()); // Blocking call, retrieves result
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Task failed with an exception: " + e.getCause().getMessage());
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("All tasks have completed. Executor has been shut down.");
    }
}
