package exercises;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Practice 603 — Bounded Buffer with ReentrantLock
 *
 * Concept: Locks, Condition variables, advanced coordination.
 * Task:
 * Implement a classic producer-consumer problem with:
 *
 * A fixed-size buffer (List<Integer> of capacity 5).
 *
 * One producer thread that adds random integers.
 *
 * One consumer thread that removes integers.
 *
 * Use ReentrantLock with two Conditions: notFull, notEmpty.
 *
 * Producer waits when buffer is full; consumer waits when empty.
 * Run for 20 items total and print every produce/consume event.
 *
 * Expected Learning:
 *
 * Difference between intrinsic locks vs ReentrantLock.
 *
 * How await() / signal() mirror wait() / notify() but give finer control.
 *
 * Importance of always unlocking in finally.
 */
public class Practice603 {

    private List<Integer> boundedBuffer = new LinkedList<>();
    int capacity = 5;
    Lock lock = new ReentrantLock();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();


    public synchronized void produce() throws InterruptedException {
        lock.lock();
        while (boundedBuffer.size() == capacity) {
            System.out.println("Bounded buffer is full");
            notEmpty.await();
        }

        IntStream.range(0, capacity).forEach(i -> {
            boundedBuffer.add(i);
            System.out.println(Thread.currentThread().getName() + " produced " + i);
            try{
                Thread.sleep(200);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });

        notEmpty.signal();
        lock.unlock();
    }

    public synchronized void consume() throws InterruptedException {
        lock.lock();
        while (boundedBuffer.size() < capacity) {
            System.out.println("Bounded buffer is less than capacity");
            notFull.await();
        }

        IntStream.range(0, capacity).forEach(i -> {
            boundedBuffer.remove(i);
            System.out.println(Thread.currentThread().getName() + " removed " + i);
            try{
                Thread.sleep(200);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });

        notFull.signal();
        lock.unlock();
    }


    public static void main(String[] args) {

    }
}
