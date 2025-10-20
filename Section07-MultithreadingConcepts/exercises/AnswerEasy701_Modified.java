package exercises;

import java.util.concurrent.Semaphore;

public class AnswerEasy701_Modified {

    private static final Semaphore semA = new Semaphore(1);
    private static final Semaphore semB = new Semaphore(0);
    private static final Semaphore semC = new Semaphore(0);

    private static final int REPEAT_COUNT = 5;

    public static void print(String letter, int i) {
        System.out.println("Step " + letter + " (" + i + ")");
    }

    public static void looper(Semaphore semToAcq, Semaphore semToRelease, String letter) {
        for (int i = 1; i <= REPEAT_COUNT; i++) {
            try {
                semToAcq.acquire();
                print(letter, i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semToRelease.release();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> looper(semA, semB, "A"));
        Thread t2 = new Thread(() -> looper(semB, semC, "B"));
        Thread t3 = new Thread(() -> looper(semC, semA, "C"));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Completed");

        /**
         * Why join() is necessary
         * In a Java program, the main thread starts all the other threads and then,
         * by default, continues its own execution. If the main thread finishes, the program
         * will terminate as soon as the last non-daemon thread finishes.
         * In your original code, the main thread starts t1, t2, and t3, prints "Completed",
         * and then exits immediately. The output "Completed" will likely appear before the looper threads have a chance to do much,
         * if anything, because the main thread's work is finished so quickly.
         * The join() method blocks the calling thread (in this case, the main thread) until
         * the thread on which it is called has completed its task. By calling join() on t1, t2, and t3,
         * you are forcing the main thread to wait for all three of them to finish their loops before it proceeds to the next line of code.
         *
         * With this change, the final output will always be the sequence of A, B, and C steps, followed by "Completed", because the main thread
         * is now guaranteed to wait for all the other threads to terminate before it prints the final message.
         */
    }
}
