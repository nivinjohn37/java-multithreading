package exercises;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueExample {

    static class Producer implements Runnable {
        private BlockingQueue<Integer> queue;

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                try {
                    queue.put(i);
                    Thread.sleep(300);
                    String threadName = Thread.currentThread().getName();
                    System.out.println(Thread.currentThread().getName() + " produced: Item-P" + threadName.charAt(threadName.length() - 2) + "-" + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    int item = queue.take();
                    Thread.sleep(3000);
                    String threadName = Thread.currentThread().getName();
                    System.out.println(Thread.currentThread().getName() + " consumed: Item-P" + threadName.charAt(threadName.length() - 2) + "-" + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(5);
        Thread producerThread1 = new Thread(new Producer(queue), "Producer-1");
        Thread producerThread2 = new Thread(new Producer(queue), "Producer-2");
        Thread consumerThread1 = new Thread(new Consumer(queue), "Consumer-1");
        Thread consumerThread2 = new Thread(new Consumer(queue), "Consumer-2");
        Thread consumerThread3 = new Thread(new Consumer(queue), "Consumer-3");
        producerThread1.start();
        producerThread2.start();
        consumerThread1.start();
        consumerThread2.start();
        consumerThread3.start();
        System.out.println("\nAll items have been consumed. The main thread can now perform a final task.");

    }
}
