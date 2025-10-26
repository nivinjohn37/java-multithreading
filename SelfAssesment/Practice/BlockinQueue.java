package SelfAssesment.Practice;

import java.util.LinkedList;
import java.util.Queue;

public class BlockinQueue<T> {
    private int capacity;
    private Queue<T> myQueue = new LinkedList<>();

    public BlockinQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue(T value) throws InterruptedException {
        while (myQueue.size() == capacity) {
            wait();
        }
        myQueue.add(value);
        System.out.printf("Value - %s added to queue by thread -%s\n", value, Thread.currentThread().getName());
        Thread.sleep(400);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (myQueue.isEmpty()) {
            wait();
        }

        T value = myQueue.poll();
        System.out.printf("Value - %s removed from queue by thread -%s\n", value, Thread.currentThread().getName());
        Thread.sleep(2000);
        notifyAll();
        return value;
    }

    public static void main(String[] args) {
        BlockinQueue<Integer> blockinQueue = new BlockinQueue<>(5);

        Thread t1 = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    blockinQueue.enqueue(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }, "producer");


        Thread t2 = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int value = blockinQueue.dequeue();
                    System.out.printf("dequeue - %s\n", value);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }, "consumer");

        t1.start();
        t2.start();
    }

}


