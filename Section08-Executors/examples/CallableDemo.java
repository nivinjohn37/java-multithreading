package examples;

import java.io.IOException;
import java.util.concurrent.*;

public class CallableDemo {

    // A Callable task that can throw a checked IOException
    static class MyCallableTask implements Callable<String> {
        private final int taskId;

        public MyCallableTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public String call() throws IOException {
            System.out.println(Thread.currentThread().getName() + " -> Executing Task " + taskId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Task interrupted", e);
            }
            if (taskId == 3) {
                throw new IOException("Simulated file not found for Task 3");
            }
            return "Task " + taskId + " completed successfully";
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String>[] futures = new Future[5];

        for (int i = 1; i <= 5; i++) {
            futures[i - 1] = executor.submit(new MyCallableTask(i));
        }

        executor.shutdown();

        // Retrieve results or exceptions from each Future
        for (Future<String> future : futures) {
            try {
                System.out.println("Result: " + future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                // The original checked IOException is wrapped inside ExecutionException
                System.err.println("Caught an exception from a task: " + e.getCause().getMessage());
            }
        }
    }
}
