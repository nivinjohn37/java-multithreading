package exercises;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Practice401 {
    /**
     * 🧩 Easy
     * Write a Java program to create two threads, each printing numbers from 1–5 with different delays. Observe the interleaving pattern due to time-slicing.
     * ⚙️ Medium
     * Create three threads representing different processes — for example:
     * <p>
     * Email sender
     * File downloader
     * Logger Simulate concurrent execution using Thread.sleep() for different durations.
     * 🔬 Hard
     * Simulate CPU time-slicing manually: Create multiple threads and pause each using sleep() to mimic round-robin scheduling.
     */
    public static void main(String[] args) {
        easyProblems();
        mediumProblems();
        hardProblems();
    }

    private static void hardProblems() {
        /**
         * 🔬 Hard
         * Simulate CPU time-slicing manually:
         * Create multiple threads and pause each using sleep() to mimic round-robin scheduling.
         */
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                IntStream.rangeClosed(0, 10)
                        .forEach(j ->{
                            try {
                                Thread.sleep(new Random().nextInt(100));
                                System.out.println(Thread.currentThread().getName() + ": " + j);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
            });
        }

        Arrays.stream(threads).forEach(Thread::start);
        /**
         * threads[i] = new Thread(() -> {
         *                 IntStream.range(0, incrementPerThread).forEach(j -> {
         *                     threadCounter.increment();
         *                     System.out.println(threadCounter.getCount());
         *                 });
         *             });
         */
    }

    private static void mediumProblems() {
        /**
         * ⚙️ Medium
         *  Create three threads representing different processes — for example:
         *  Email sender
         *  File downloader
         *  Logger Simulate
         *  concurrent execution using Thread.sleep() for different durations.
         */

        Thread emailSender = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("[Email Sender] Sending part " + i + "/5");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[Email Sender] Completed ✅");
        });

        Thread fileDownloader = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("[File Downloader Sender] Sending part " + i + "/5");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[Email Sender] Completed ✅");
        });

        Thread logger = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("[Logger] Sending part " + i + "/5");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[Email Sender] Completed ✅");
        });

        emailSender.start();
        fileDownloader.start();
        logger.start();

        try {
            emailSender.join();
            fileDownloader.join();
            logger.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void easyProblems() {
        /**
         *  🧩 Easy
         *  Write a Java program to create two threads, each printing numbers from 1–5
         *  with different delays. Observe the interleaving pattern due to time-slicing.
         */
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");

        t1.start();
        t2.start();
        /**
         * Output
         * t1: 1
         * t2: 1
         * t2: 2
         * t1: 2
         * t2: 3
         * t1: 3
         * t2: 4
         * t1: 4
         * t2: 5
         * t1: 5
         */
    }
}
