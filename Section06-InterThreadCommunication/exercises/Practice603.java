package exercises;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Practice 603 — Bounded Buffer with ReentrantLock
 * <p>
 * Concept: Locks, Condition variables, advanced coordination.
 * Task:
 * Implement a classic producer-consumer problem with:
 * <p>
 * A fixed-size buffer (List<Integer> of capacity 5).
 * <p>
 * One producer thread that adds random integers.
 * <p>
 * One consumer thread that removes integers.
 * <p>
 * Use ReentrantLock with two Conditions: notFull, notEmpty.
 * <p>
 * Producer waits when buffer is full; consumer waits when empty.
 * Run for 20 items total and print every produce/consume event.
 * <p>
 * Expected Learning:
 * <p>
 * Difference between intrinsic locks vs ReentrantLock.
 * <p>
 * How await() / signal() mirror wait() / notify() but give finer control.
 * <p>
 * Importance of always unlocking in finally.
 */
public class Practice603 {

    private List<Integer> boundedBuffer = new LinkedList<>();
    int capacity = 5;
    Lock lock = new ReentrantLock();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    private int totalItems = 20;
    private int value = 0;


    public void produce()  {

        for (int i = 0; i < totalItems; i++) {
            lock.lock();
            try {
                while (boundedBuffer.size() == capacity) {
                    System.out.println("Bounded buffer is full");
                    notFull.await();
                }

                boundedBuffer.add(value);
                System.out.println("Produced " + value + " Thread -> " + Thread.currentThread().getName());

                value++;
                Thread.sleep(200);
                notEmpty.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

    public void consume() {

        for (int i = 0; i < totalItems; i++) {
            lock.lock();
            try {
                while (boundedBuffer.isEmpty()) {
                    System.out.println("Bounded buffer is empty - nothing to consume");
                    notEmpty.await();
                }

                int item = boundedBuffer.remove(0);
                System.out.println("Consumed " + item + " Thread -> " + Thread.currentThread().getName());
                Thread.sleep(200);
                notFull.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }


    public static void main(String[] args) {
        Practice603 practice603 = new Practice603();
        Thread t1 = new Thread(practice603::produce, "producer");
        Thread t2 = new Thread(practice603::consume, "consumer");

        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
