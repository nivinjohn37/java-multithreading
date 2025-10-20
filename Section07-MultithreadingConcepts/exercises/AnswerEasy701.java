package exercises;

import java.util.concurrent.Semaphore;

public class AnswerEasy701 {

    private static final Semaphore semA = new Semaphore(1);
    private static final Semaphore semB = new Semaphore(0);
    private static final Semaphore semC = new Semaphore(0);

    private static final int REPEAT_COUNT = 5;

    public static void print(String letter, int i) {
        System.out.println("Step " + letter + " (" + i + ")");
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= REPEAT_COUNT; i++) {
                try {
                    semA.acquire();
                    print("A", i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semB.release();
                }
            }
        }, "A");

        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= REPEAT_COUNT; i++) {
                try {
                    semB.acquire();
                    print("B", i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semC.release();
                }
            }
        }, "B");

        Thread t3 = new Thread(() -> {
            for (int i = 1; i <= REPEAT_COUNT; i++) {
                try {
                    semC.acquire();
                    print("C", i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semA.release();
                }
            }
        }, "C");

        t1.start();
        t2.start();
        t3.start();
    }
}

