package exercises;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Practice509 {
    /**
     * Practice 509 — Manual Round-Robin Scheduling
     * <p>
     * Concept: Controlled time-slicing, fairness.
     * <p>
     * Task:
     * <p>
     * Create 5 threads printing messages in steps (1–5).
     * <p>
     * Each thread sleeps 100 ms after each step.
     * <p>
     * Observe rotation pattern of threads getting CPU time.
     * <p>
     * Learning:
     * Mimics OS round-robin scheduling.
     */
    public static void main(String[] args) {
        int numThreads = 5;
        int numSteps = 5;
        Thread[] threads = new Thread[numThreads];
        IntStream.range(0, numThreads)
                .forEach(i -> threads[i] = new Thread(() -> {
                    for (int j = 0; j < numSteps; j++) {
                        try {
                            System.out.println("Thread " + i + ": " + j);
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }));

        Stream.of(threads).forEach(Thread::start);


    }
}
