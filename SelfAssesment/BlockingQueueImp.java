package SelfAssesment;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueueImp {
    static class BlockingQueueCustom<T> {
        private int capacity;
        private final Queue<T> queue = new LinkedList<T>();

        public BlockingQueueCustom(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void enqueue(T value) throws InterruptedException {
            while (queue.size() == capacity) {
                wait();
            }
            queue.add(value);
            System.out.println("Enqueue " + value);
            Thread.sleep(400);
            notifyAll();
        }

        public synchronized void dequeue() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            T value = queue.remove();
            System.out.println("Dequeue " + value);
            Thread.sleep(2000);
            notifyAll();
        }

    }

    public static void main(String[] args) {
        BlockingQueueCustom<Integer> queue = new BlockingQueueCustom<>(10);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.enqueue(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try{
                    queue.dequeue();
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        System.out.println(queue.capacity + "completed");
    }
}
