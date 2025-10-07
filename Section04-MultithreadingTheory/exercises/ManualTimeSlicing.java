package exercises;

import java.util.Arrays;

public class ManualTimeSlicing {
    public static void main(String[] args) {
        final int THREAD_COUNT = 3;
        final int SLICE_TIME = 100; // milliseconds
        final int ITERATIONS = 5;

        // Create threads
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            int threadId = i; // effectively final for lambda
            threads[i] = new Thread(() -> {
                for (int j = 1; j <= ITERATIONS; j++) {
                    System.out.println("Thread-" + threadId + " executing step " + j);

                    try {
                        // Simulate the CPU giving time slice
                        Thread.sleep(SLICE_TIME);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("Thread-" + threadId + " finished.");
            }, "T-" + i);
        }

        // Start all threads
        Arrays.stream(threads).forEach(Thread::start);
    }
}
