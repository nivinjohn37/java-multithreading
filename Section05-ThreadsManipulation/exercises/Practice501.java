package exercises;

/**
 * Practice 501 — Start vs Run
 *
 * Concept: Thread creation, difference between run() and start()
 *
 * Task:
 *
 * Create a class Printer that implements Runnable.
 *
 * In its run() method, print numbers 1–5 with the current thread name.
 *
 * In main(),
 *
 * call run() directly
 *
 * then call start() on a new thread object.
 * Observe the difference in output.
 *
 * Expected Insight:
 * run() executes sequentially in the main thread,
 * start() runs concurrently in a new thread.
 */
public class Practice501 {
    static class Printer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }
    }

    public static void main(String[] args) {
        Printer printer = new Printer();
        printer.run();
        //  t1.run();
        /**
         * main: 0
         * main: 1
         * main: 2
         * main: 3
         * main: 4
         */

        //t1.start();
        /**
         * Thread-0: 0
         * Thread-0: 1
         * Thread-0: 2
         * Thread-0: 3
         * Thread-0: 4
         */
    }

}
