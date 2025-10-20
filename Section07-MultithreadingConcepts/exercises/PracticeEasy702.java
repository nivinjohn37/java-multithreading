package exercises;

import java.util.concurrent.Semaphore;

/**
 * 🧩 Practice 702 — Parking Lot Controller
 *
 * 🎯 Concept: Counting semaphore, resource limitation
 * 💡 Goal: Manage limited concurrent access.
 *
 * 🧠 Task:
 *
 * Simulate a parking lot with 3 spots using a Semaphore(3).
 *
 * 6 cars (threads) arrive simultaneously.
 *
 * Each car:
 *
 * Tries to enter the parking lot.
 *
 * Stays “parked” for a random time (sleep).
 *
 * Leaves the lot and frees up a space.
 *
 * ✅ Expected Output (order may vary):
 *
 * Car 1 entered (spots left: 2)
 * Car 2 entered (spots left: 1)
 * Car 3 entered (spots left: 0)
 * Car 4 waiting...
 * Car 1 leaving (spots left: 1)
 * Car 4 entered (spots left: 0)
 * ...
 *
 * 🔍 Hints:
 *
 * Use semaphore.acquire() before entering.
 *
 * Use semaphore.release() when leaving.
 *
 * Use semaphore.availablePermits() to show remaining spots.
 *
 * 🧩 Learning:
 *
 * You’ll understand counting semaphores and controlled concurrency.
 */
public class PracticeEasy702 {
    public static final Semaphore semaphore = new Semaphore(3);
    public static final int TOTAL_CARS = 10;

    public static void main(String[] args) {
        for (int i = 0; i < TOTAL_CARS; i++) {
            Thread c1 = new Thread(() -> {
                try{
                    semaphore.acquire();
                    System.out.printf("%s entered (spots left: %d) " , Thread.currentThread().getName(), semaphore.availablePermits());
                    System.out.println();
                    Thread.sleep((int)(Math.random() * 3000 + 1000)); // random parking time
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release();
                }
            }, "Car " + i);
            c1.start();
        }
    }
}
