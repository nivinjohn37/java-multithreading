package examples;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

public class ParallelTaskExecutorTest {
    static final int totalTasks = 50;

    static class Task implements Callable<String> {

        int id;

        public Task(int id) {
            this.id = id;
        }

        @Override
        public String call() {
            System.out.println("Executing Task " + id + " on thread: " + Thread.currentThread().getName());

            try {
                // Simulate work with a random delay
                Thread.sleep(500 + (long) (Math.random() * 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "This is a task -> " + id;
        }
    }

    public static void main(String[] args) {
        LocalTime currentTime = LocalTime.now();
        System.out.println("Current local time: " + currentTime);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        //Future<String>[] futures = new Future[totalTasks];
        List<Future<?>> futures = new ArrayList<>(totalTasks);

        for (int i = 1; i <= totalTasks; i++) {
            futures.add(executor.submit(new Task(i)));

        }
        System.out.println("All tasks submitted. Waiting for completion...");
        try {
            for (Future<?> future : futures) {
                System.out.println(future.get()); // Blocking call, retrieves result
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Task failed with an exception: " + e.getCause().getMessage());
        }


        // Wait for all tasks to complete and then shut down the executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force shutdown if timeout is reached
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        LocalTime currentTime2 = LocalTime.now();
        System.out.println("Current local time2: " + currentTime2);
        Duration duration = Duration.between(currentTime, currentTime2);
        System.out.println("Duration: " + duration.toSeconds());

        System.out.println("All tasks have completed. Executor has been shut down.");
    }
}