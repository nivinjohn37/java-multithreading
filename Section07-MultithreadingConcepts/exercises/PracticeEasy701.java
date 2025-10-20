package exercises;

import java.util.concurrent.Semaphore;

/**
 * 🟢 EASY LEVEL
 * 🧩 Practice 701 — Print with Semaphore
 *
 * 🎯 Concept: Basic semaphore control for sequential execution
 * 💡 Goal: Learn how to make threads run in a specific order.
 *
 * 🧠 Task:
 *
 * Create 3 threads:
 *
 * Thread A → prints "Step A"
 *
 * Thread B → prints "Step B"
 *
 * Thread C → prints "Step C"
 *
 * Use semaphores so the output is always:
 *
 * Step A
 * Step B
 * Step C
 *
 *
 * Repeat this sequence 5 times.
 *
 * 🔍 Hints:
 *
 * Use 3 semaphores: semA, semB, semC
 *
 * Initialize:
 * semA = 1, semB = 0, semC = 0
 *
 * After A prints, it releases semB, and so on cyclically.
 *
 * 🧩 Learning:
 *
 * You’ll see how semaphores can enforce strict ordering, even without locks or join().
 */
public class PracticeEasy701 {

    public static final Semaphore semA = new Semaphore(1);
    public static final Semaphore semB = new Semaphore(0);
    public static final Semaphore semC = new Semaphore(0);

    public static void print(String letter){
        System.out.println("STEP " + letter);
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                try{
                    semA.acquire();
                    print("A");
                }catch(InterruptedException e){
                    System.out.println(e);
                }finally {
                    semB.release();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                try{
                    semB.acquire();
                    print("B");
                }catch(InterruptedException e){
                    System.out.println(e);
                }finally {
                    semC.release();
                }
            }
        });

        Thread t3 = new Thread(() -> {
            while (true) {
                try{
                    semC.acquire();
                    print("C");
                }catch(InterruptedException e){
                    System.out.println(e);
                }finally {
                    semA.release();
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        }catch(InterruptedException e){
            System.out.println(e);
        }
    }
}
