package exercises;

public class Practice502 {
    /**
     * Concept: join(), sequential vs concurrent flow.
     * <p>
     * Task:
     * <p>
     * Create two threads doing simulated work (Thread.sleep(1000) per step).
     * <p>
     * Measure total time when:
     * <p>
     * You start them sequentially (t1.run(); t2.run();)
     * <p>
     * You start them concurrently (t1.start(); t2.start(); t1.join(); t2.join();)
     * <p>
     * Expected Output:
     * Parallel execution should take roughly half the sequential time.
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {

                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "--");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.run();
    }

}
