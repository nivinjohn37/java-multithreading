package exercises;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 🧩 Practice 703 — Print Even and Odd with Mutex
 * <p>
 * 🎯 Concept: Mutual exclusion with ReentrantLock
 * 💡 Goal: Two threads alternately print even and odd numbers safely.
 * <p>
 * 🧠 Task:
 * <p>
 * Thread 1 prints odd numbers (1, 3, 5, …)
 * <p>
 * Thread 2 prints even numbers (2, 4, 6, …)
 * <p>
 * Print numbers from 1–20 in perfect order.
 * <p>
 * ✅ Output:
 * <p>
 * Odd: 1
 * Even: 2
 * Odd: 3
 * Even: 4
 * ...
 * <p>
 * 🔍 Hints:
 * <p>
 * Use a shared counter variable.
 * <p>
 * Use Lock and Condition objects for coordination.
 * <p>
 * Signal the next thread after printing.
 * <p>
 * 🧩 Learning:
 * <p>
 * Learn explicit locking and condition signaling with fine control — similar to wait()/notify() but cleaner.
 */
public class PracticeMed703 {

    public static AtomicInteger count = new AtomicInteger(1);
    Lock lock = new ReentrantLock();
    Condition isEven = lock.newCondition();
    Condition isOdd = lock.newCondition();

    public void odd() {
        if (lock.tryLock()) {
            try {
                while (count.get() % 2 == 0) {
                    isOdd.await();
                }
                System.out.println("Odd: " + count.getAndIncrement());
                Thread.sleep(1000);
                isEven.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public void even() {
        if (lock.tryLock()) {
            try {
                while (count.get() % 2 != 0) {
                    isEven.await();
                }
                System.out.println("Even: " + count.getAndIncrement());
                Thread.sleep(1000);
                isOdd.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        PracticeMed703 p = new PracticeMed703();
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 10; i++){
                p.odd();
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0; i < 10; i++){
                p.even();
            }
        });

        t1.start();
        t2.start();
    }

}
