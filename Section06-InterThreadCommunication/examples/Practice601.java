package examples;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 🟢 Easy Level (Foundations of Synchronization)
 * <p>
 * Practice 601 — Safe Counter with Synchronized
 * <p>
 * Concept: Object-level locking, thread safety.
 * Task:
 * Create a Counter class with:
 * •	a private int count,
 * •	an increment() method that adds 1 with synchronization,
 * •	a getCount() method that returns the value.
 * <p>
 * Start 3 threads, each incrementing 1000 times.
 * Print the final count and confirm it’s 3000.
 * <p>
 * Expected Learning:
 * •	How synchronized guarantees mutual exclusion.
 * •	How race conditions appear if you remove synchronized.
 */
public class Practice601 {

    static class Counter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }


    public static void main(String[] args) {
        Counter counter = new Counter();


        Thread[] threads = IntStream.range(0, 3)
                .mapToObj(i -> new Thread(() -> {
                    incrementInLoop(counter);
                }))
                //.toArray(size -> new Thread[size])
                .toArray(Thread[]::new);




        Arrays.stream(threads).forEach(Thread::start);


        Arrays.stream(threads).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        System.out.println(counter.getCount());
    }

    private static Counter incrementInLoop(Counter counter) {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }
        return counter;
    }

}
