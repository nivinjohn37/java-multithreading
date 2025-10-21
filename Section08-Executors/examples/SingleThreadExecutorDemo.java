package examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutorDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " → Executing Task " + taskId);
                try {
                    Thread.sleep(1000);
                    throw new RuntimeException("Simulated failure");

                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " <UNK> Interrupted " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
    }
}
