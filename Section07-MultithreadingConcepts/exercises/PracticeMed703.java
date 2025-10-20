package exercises;

/**
 * 🧩 Practice 703 — Print Even and Odd with Mutex
 *
 * 🎯 Concept: Mutual exclusion with ReentrantLock
 * 💡 Goal: Two threads alternately print even and odd numbers safely.
 *
 * 🧠 Task:
 *
 * Thread 1 prints odd numbers (1, 3, 5, …)
 *
 * Thread 2 prints even numbers (2, 4, 6, …)
 *
 * Print numbers from 1–20 in perfect order.
 *
 * ✅ Output:
 *
 * Odd: 1
 * Even: 2
 * Odd: 3
 * Even: 4
 * ...
 *
 * 🔍 Hints:
 *
 * Use a shared counter variable.
 *
 * Use Lock and Condition objects for coordination.
 *
 * Signal the next thread after printing.
 *
 * 🧩 Learning:
 *
 * Learn explicit locking and condition signaling with fine control — similar to wait()/notify() but cleaner.
 */
public class PracticeMed703 {
}
